package com.mc851.xcommerce.model.internal

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class PaymentStatus(val _id: Int) {
    PENDING(1),
    OK(2),
    CANCELED(3),
    ERROR(Integer.MAX_VALUE);

    @JsonValue
    fun getId() = _id

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): PaymentStatus {
            return (PaymentStatus::class.java).enumConstants.first { value == it.getId() }
                   ?: throw IllegalArgumentException("Invalid id for enum")
        }

    }
}

enum class ShipmentStatus(val _id: Int) {
    NOT_STARTED(1),
    SHIPPED(2),
    DELIVERED(3),
    ERROR(Integer.MAX_VALUE);

    @JsonValue
    fun getId() = _id

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): ShipmentStatus {
            return (ShipmentStatus::class.java).enumConstants.first { value == it.getId() }
                   ?: throw IllegalArgumentException("Invalid id for enum")
        }

    }
}