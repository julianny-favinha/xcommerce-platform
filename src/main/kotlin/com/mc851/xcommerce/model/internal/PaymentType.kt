package com.mc851.xcommerce.model.internal

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class PaymentType(private val _id: Int) {
    CREDIT_CARD(1),
    BOLETO(2);

    @JsonValue
    fun getId() = _id

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): PaymentType {
            return (PaymentType::class.java).enumConstants.first { value == it.getId() }
                   ?: throw IllegalArgumentException("Invalid id for enum")
        }

    }

}