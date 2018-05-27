package com.mc851.xcommerce.service.cart

import com.mc851.xcommerce.model.api.CheckoutIn
import com.mc851.xcommerce.model.api.CheckoutOut
import com.mc851.xcommerce.model.internal.BoletoPayment
import com.mc851.xcommerce.model.internal.CreditCardInfo
import com.mc851.xcommerce.model.internal.CreditCardPayment
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
            TODO()
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
        val order = orderService.retrieveOrder(orderId)

        val paymentResult = when (checkout.paymentInfo.paymentType) {
            PaymentType.CREDIT_CARD -> creditCardPayment(checkout.paymentInfo.creditCardInfo,
                userInfo,
                order.freightPrice + order.productsPrice,
                checkout.paymentInfo.installments)
            PaymentType.BOLETO -> boletoPayment(userInfo, order.freightPrice + order.productsPrice)
        }

    }

    private fun creditCardPayment(creditCardInfo: CreditCardInfo?,
                                  userInfo: UserInfo,
                                  value: Long,
                                  installment: Long?) =
        paymentService.payCreditCard(CreditCardPayment(creditCardInfo!!, userInfo, value, installment!!))

    private fun boletoPayment(userInfo: UserInfo, value: Long) =
        paymentService.payBoleto(BoletoPayment(userInfo, value))

}