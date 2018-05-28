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
import com.mc851.xcommerce.service.user.UserService
import mu.KotlinLogging

class CartService(private val checkoutValidator: CheckoutValidator,
                  private val orderService: OrderService,
                  private val paymentService: PaymentService,
                  private val userService: UserService) {

    private val log = KotlinLogging.logger {}

    fun checkout(checkoutIn: CheckoutIn, userId: Long): CheckoutOut {

        log.info { "Starting checkout with $checkoutIn for user $userId" }

        val validationResult = checkoutValidator.validate(checkoutIn.cart, userId)
        if (!validationResult.status.success()) {
            TODO("Create validator and return checkout -> NOT_OK")
        }

        val products = checkoutIn.cart.cartItems.map {
            it.product
        }

        val orderId = orderService.registerOrder(products, userId, checkoutIn.shipmentInfo)
        return purchaseOrder(orderId, userId, checkoutIn)
    }

    private fun purchaseOrder(orderId: Long, userId: Long, checkout: CheckoutIn): CheckoutOut {
        val user = userService.findByUserId(userId)
        val userInfo = UserInfo(user.cpf, user.name, "BLABLBA", "13083-852")
        //TODO("Deal with address")
        val order = orderService.retrieveOrder(orderId)

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
                CheckoutOut(CheckoutStatus.NOT_OK)
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