package com.mc851.xcommerce.service.cart.validators

import com.mc851.xcommerce.model.api.CheckoutIn
import com.mc851.xcommerce.model.internal.PaymentType
import com.mc851.xcommerce.model.internal.ValidationResult
import com.mc851.xcommerce.model.internal.ValidationStatus
import com.mc851.xcommerce.service.product.ProductService
import com.mc851.xcommerce.service.user.AddressService
import com.mc851.xcommerce.service.user.CreditService
import com.mc851.xcommerce.service.user.UserService
import mu.KotlinLogging
import java.util.*

class CheckoutValidator(private val userService: UserService,
                        private val productService: ProductService,
                        private val addressService: AddressService) {

    fun validate(checkout: CheckoutIn, userId: Long): ValidationResult {
        val user = userService.findByUserId(userId) ?: return ValidationResult(ValidationStatus.INVALID_USER)


        checkout.cart.cartItems.map { cartItem ->
            productService.getById(cartItem.product.id) ?: return ValidationResult(ValidationStatus.INVALID_PRODUCTS)
        }

        if (checkout.paymentInfo.installments != null) {
            if (checkout.paymentInfo.installments > 10L || checkout.paymentInfo.installments < 0) {
                return ValidationResult(ValidationStatus.INVALID_PAYMENT_PARAMETERS)
            }
        }


        if (checkout.paymentInfo.paymentType == PaymentType.CREDIT_CARD) {
            val creditCardInfo = checkout.paymentInfo.creditCardInfo
                    ?: return ValidationResult(ValidationStatus.INVALID_PAYMENT_PARAMETERS)
            if (creditCardInfo.cardNumber.length != 16) {
                return ValidationResult(ValidationStatus.INVALID_PAYMENT_PARAMETERS)
            }

            if (creditCardInfo.cvv.length != 3) {
                return ValidationResult(ValidationStatus.INVALID_PAYMENT_PARAMETERS)
            }

            if (creditCardInfo.month < 1 || creditCardInfo.month > 12) {
                return ValidationResult(ValidationStatus.INVALID_PAYMENT_PARAMETERS)
            }

            if (creditCardInfo.year.toInt() < Calendar.getInstance().get(Calendar.YEAR)) {
                return ValidationResult(ValidationStatus.INVALID_PAYMENT_PARAMETERS)
            }

            if (creditCardInfo.year.toInt() == Calendar.getInstance().get(Calendar.YEAR) && creditCardInfo.month.toInt() - 1 < Calendar.getInstance().get(Calendar.MONTH)) {
                return ValidationResult(ValidationStatus.INVALID_PAYMENT_PARAMETERS)
            }
        }

        val cep = checkout.shipmentInfo.cepDst
        addressService.checkCep(cep) ?: return ValidationResult(ValidationStatus.INVALID_SHIPMENT_PARAMETERS)

        return ValidationResult(ValidationStatus.OK)
    }

}