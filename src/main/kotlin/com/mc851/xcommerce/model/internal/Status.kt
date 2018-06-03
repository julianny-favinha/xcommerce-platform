package com.mc851.xcommerce.model.internal

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class PaymentStatus(private val _id: Int) {
    PENDING(1),
    OK(2),
    EXPIRED(3),
    CANCELED(4),
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
    PREPARING_FOR_SHIPMENT(2),
    SHIPPED(3),
    DELIVERED(4),
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