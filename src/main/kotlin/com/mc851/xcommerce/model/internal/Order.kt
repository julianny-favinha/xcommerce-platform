package com.mc851.xcommerce.model.internal

import com.mc851.xcommerce.model.api.ShipmentInfo
import java.sql.Timestamp

data class OrderValue(val freightPrice: Long, val productsPrice: Long, val userId: Long, val shipmentInfo: ShipmentInfo, val paymentType: PaymentType)

data class Order(val id: Long,
                 val freightPrice: Long,
                 val productsPrice: Long,
                 val userId: Long,
                 val paymentCode: String?,
                 val paymentStatus: PaymentStatus,
                 val paymentType: PaymentType,
                 val shipmentId: Long?,
                 val shipmentStatus: ShipmentStatus,
                 val shipmentInfo: ShipmentInfo,
                 val createdAt: Timestamp)

data class OrderItemValue(val orderId: Long, val productId: Long, val productName: String, val productPrice: Long)

data class OrderItem(val id: Long,
                     val orderId: Long,
                     val productId: Long,
                     val productName: String,
                     val productPrice: Long)