package com.mc851.xcommerce.model.api

import com.mc851.xcommerce.model.internal.PaymentStatus
import com.mc851.xcommerce.model.internal.PaymentType
import com.mc851.xcommerce.model.internal.ShipmentStatus
import com.mc851.xcommerce.model.internal.ShipmentType

data class Orders(val orders: List<Order>)

data class Order(val productsByQuantity: Map<Product, Long>,
                 val orderId: Long,
                 val totalPrice: Long,
                 val freightPrice: Long,
                 val totalQuantity: Long,
                 val shipmentInfo: ShipmentInfo,
                 val shipmentStatus: ShipmentStatus,
                 val paymentStatus: PaymentStatus,
                 val paymentType: PaymentType,
                 val barcode: String?,
                 val createdAt: String)