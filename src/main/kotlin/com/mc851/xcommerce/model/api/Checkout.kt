package com.mc851.xcommerce.model.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.mc851.xcommerce.model.internal.CreditCardInfo
import com.mc851.xcommerce.model.internal.PaymentType
import com.mc851.xcommerce.model.internal.ShipmentType
import com.mc851.xcommerce.model.internal.ValidationStatus

data class CheckoutIn(val cart: Cart, val paymentInfo: PaymentInfo, val shipmentInfo: ShipmentInfo)

data class PaymentInfo(val paymentType: PaymentType, val creditCardInfo: CreditCardInfo?, val installments: Long?)

data class ShipmentInfo(val cepDst: String, val shipmentType: ShipmentType)

data class CheckoutOut(val orderId: Long?, val status: CheckoutStatus, val paymentDetails: PaymentOut?, val validation_status: ValidationStatus?) {
    constructor(status: CheckoutStatus) : this(null, status, null, null)
    constructor(orderId: Long, status: CheckoutStatus): this(orderId, status, null, null)
    constructor(status: CheckoutStatus, validation_status: ValidationStatus): this(null, status, null, validation_status)
    constructor(orderId: Long, status: CheckoutStatus, paymentDetails: PaymentOut): this(orderId, status, paymentDetails, null)
}

data class PaymentOut(val barCode: String)

enum class CheckoutStatus(val _id: Int) {
    OK(1),
    PENDING(2),
    BAD_INPUT(3),
    FAILED(4);

    @JsonValue
    fun getId() = _id

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): CheckoutStatus {
            return (CheckoutStatus::class.java).enumConstants.first { value == it.getId() }
                   ?: throw IllegalArgumentException("Invalid id for enum")
        }

    }
}