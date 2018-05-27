package com.mc851.xcommerce.service.cart

import com.mc851.xcommerce.model.api.CheckoutIn
import com.mc851.xcommerce.model.api.CheckoutOut
import com.mc851.xcommerce.model.api.CheckoutStatus
import com.mc851.xcommerce.model.api.PaymentOut
import com.mc851.xcommerce.model.internal.BoletoPayment
import com.mc851.xcommerce.model.internal.CreditCardInfo
import com.mc851.xcommerce.model.internal.CreditCardPayment
import com.mc851.xcommerce.model.internal.PaymentResult
import com.mc851.xcommerce.model.internal.PaymentType
import com.mc851.xcommerce.model.internal.UserInfo
import com.mc851.xcommerce.service.cart.validators.CheckoutValidator
import com.mc851.xcommerce.service.order.OrderService
import com.mc851.xcommerce.service.payment.PaymentService
import com.mc851.xcommerce.service.user.UserService

class CartService(private val checkoutValidator: CheckoutValidator,
                  private val orderService: OrderService,
                  private val paymentService: PaymentService,
                  private val userService: UserService) {

    fun checkout(checkoutIn: CheckoutIn, userId: Long): CheckoutOut {

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
        val user = userService.findByUserId(userId) ?: throw IllegalStateException("User must exist!")
        val userInfo = UserInfo(user.cpf, user.name, "BLABLBA", "13083-852")
        //TODO("Deal with address")
        val order = orderService.retrieveOrder(orderId)

        val paymentResult = when (checkout.paymentInfo.paymentType) {
            PaymentType.CREDIT_CARD -> creditCardPayment(checkout.paymentInfo.creditCardInfo,
                userInfo,
                order.freightPrice + order.productsPrice,
                checkout.paymentInfo.installments)
            PaymentType.BOLETO -> boletoPayment(userInfo, order.freightPrice + order.productsPrice)
        }
        // TODO("Must return something better than PaymentResult, with payment Id, Payment Result, and Details")
        val paymentId = 1L

        orderService.registerPayment(orderId, paymentId)
        orderService.updatePaymentStatus(orderId, paymentResult.paymentStatus)

        return when (paymentResult) {
            PaymentResult.PENDING -> CheckoutOut(order.id,
                CheckoutStatus.OK,
                PaymentOut("1302913901239120312930193213210392109"))
            PaymentResult.FAILED, PaymentResult.ERROR -> {
                cancelOrder(orderId)
                CheckoutOut(null, CheckoutStatus.NOT_OK, null)
            }
            PaymentResult.AUTHORIZED -> CheckoutOut(order.id, CheckoutStatus.OK, null)
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