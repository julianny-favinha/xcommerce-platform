package com.mc851.xcommerce.model.internal

data class ValidationResult(val status: ValidationStatus)

enum class ValidationStatus {
    OK,
    INVALID_USER,
    INVALID_PRODUCTS,
    PRODUCTS_UNAVAILABLE,
    INVALID_PAYMENT_PARAMETERS,
    INVALID_SHIPMENT_PARAMETERS;

    fun success() = this == OK

}