package com.mc851.xcommerce.service.cart

import com.mc851.xcommerce.model.api.CheckoutIn
import com.mc851.xcommerce.model.api.CheckoutOut
import com.mc851.xcommerce.service.cart.validators.CheckoutValidator
import com.mc851.xcommerce.service.user.UserService
import java.util.UUID

class CartService(private val checkoutValidator: CheckoutValidator,
                  private val userService: UserService) {

    fun checkout(checkoutIn: CheckoutIn, userId: Long) : CheckoutOut {

        val validationResult = checkoutValidator.validate(checkoutIn.cart, userId)
        if (!validationResult.status.success()){
            return TODO()
        }

        TODO()
    }

    fun preCheckout() : String {
        val cartId = UUID.randomUUID()

        return cartId.toString()
    }
}