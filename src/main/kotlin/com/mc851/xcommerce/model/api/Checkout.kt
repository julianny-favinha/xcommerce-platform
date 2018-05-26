package com.mc851.xcommerce.model.api

import com.mc851.xcommerce.model.internal.CreditCardInfo
import com.mc851.xcommerce.model.internal.PaymentType
import com.mc851.xcommerce.model.internal.ShipmentType

data class CheckoutIn(val cart: Cart, val paymentInfo: PaymentInfo, val shipmentInfo: ShipmentInfo)

data class PaymentInfo(val paymentType: PaymentType, val creditCardInfo: CreditCardInfo?)

data class ShipmentInfo(val shipmentType: ShipmentType)

data class CheckoutOut(val status: Long)