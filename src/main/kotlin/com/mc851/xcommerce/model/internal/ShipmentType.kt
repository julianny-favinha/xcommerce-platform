package com.mc851.xcommerce.model.internal

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class ShipmentType(private val _id: Int) {
    PAC(1),
    SEDEX(2);

    @JsonValue
    fun getId() = _id

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: Int): ShipmentType {
            return (ShipmentType::class.java).enumConstants.first { value == it.getId() }
                   ?: throw IllegalArgumentException("Invalid id for enum")
        }

    }

}