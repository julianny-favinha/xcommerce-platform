package com.mc851.xcommerce.service.cart

import com.mc851.xcommerce.model.api.CheckoutIn
import com.mc851.xcommerce.model.api.CheckoutOut
import com.mc851.xcommerce.model.api.CheckoutStatus
import com.mc851.xcommerce.model.api.PaymentOut
import com.mc851.xcommerce.model.internal.BoletoPayment
import com.mc851.xcommerce.model.internal.CreditCardInfo
import com.mc851.xcommerce.model.internal.CreditCardPayment
import com.mc851.xcommerce.model.internal.PaymentResultStatus
import com.mc851.xcommerce.model.internal.PaymentType
import com.mc851.xcommerce.model.internal.UserInfo
import com.mc851.xcommerce.service.cart.validators.CheckoutValidator
import com.mc851.xcommerce.service.order.OrderService
import com.mc851.xcommerce.service.payment.PaymentService
import com.mc851.xcommerce.service.product.ProductService
import com.mc851.xcommerce.service.user.UserService
import mu.KotlinLogging

class CartService(private val checkoutValidator: CheckoutValidator,
                  private val orderService: OrderService,
                  private val paymentService: PaymentService,
                  private val userService: UserService,
                  private val productService: ProductService) {

    private val log = KotlinLogging.logger {}

    fun checkout(checkoutIn: CheckoutIn, userId: Long): CheckoutOut {

        log.info { "Starting checkout with $checkoutIn for user $userId" }

        val validationResult = checkoutValidator.validate(checkoutIn, userId)
        if (!validationResult.status.success()) {
            return CheckoutOut(CheckoutStatus.BAD_INPUT, validationResult.status)
        }

        val productsByQuantity = checkoutIn.cart.cartItems.map {
            it.product to it.quantity
        }.toMap()

        val orderId = orderService.registerOrder(productsByQuantity, userId, checkoutIn.shipmentInfo, checkoutIn.paymentInfo.paymentType)
                ?: return CheckoutOut(CheckoutStatus.FAILED)

        productService.removeExpire(productsByQuantity)

        return purchaseOrder(orderId, userId, checkoutIn)
    }

    private fun purchaseOrder(orderId: Long, userId: Long, checkout: CheckoutIn): CheckoutOut {
        val user = userService.findByUserId(userId) ?: throw IllegalStateException("What?")
        val userInfo = UserInfo(user.cpf, user.name, user.address.address.logradouro, checkout.shipmentInfo.cepDst)
        val order = orderService.retrieveOrder(orderId) ?: throw IllegalStateException("Ahm?")

        log.info { "Starting payment for order $order for checkout $checkout" }

        val paymentResult = when (checkout.paymentInfo.paymentType) {
            PaymentType.CREDIT_CARD -> creditCardPayment(checkout.paymentInfo.creditCardInfo,
                    userInfo,
                    order.freightPrice + order.productsPrice,
                    checkout.paymentInfo.installments)
            PaymentType.BOLETO -> boletoPayment(userInfo, order.freightPrice + order.productsPrice)
        }

        log.info { "Payment result $paymentResult for $order " }
        paymentResult.code?.let { orderService.registerPayment(orderId, paymentResult.code) }
        orderService.updatePaymentStatus(orderId, paymentResult.status.paymentStatus)

        return when (paymentResult.status) {
            PaymentResultStatus.PENDING -> CheckoutOut(order.id, CheckoutStatus.OK, PaymentOut(paymentResult.code!!))
            PaymentResultStatus.FAILED, PaymentResultStatus.ERROR -> {
                cancelOrder(orderId)
                CheckoutOut(order.id, CheckoutStatus.FAILED)
            }
            PaymentResultStatus.AUTHORIZED -> CheckoutOut(order.id, CheckoutStatus.OK)
        }

    }

    private fun creditCardPayment(creditCardInfo: CreditCardInfo?,
                                  userInfo: UserInfo,
                                  value: Long,
                                  installment: Long?) =
            paymentService.payCreditCard(CreditCardPayment(creditCardInfo!!, userInfo, value, installment!!))

    private fun boletoPayment(userInfo: UserInfo, value: Long) =
            paymentService.payBoleto(BoletoPayment(userInfo, value))

    private fun cancelOrder(orderId: Long) = orderService.cancelOrder(orderId)
}