package com.mc851.xcommerce.model.internal

import com.mc851.xcommerce.model.api.ShipmentInfo

data class OrderValue(val freightPrice: Long, val productsPrice: Long, val userId: Long, val shipmentInfo: ShipmentInfo)

data class Order(val id: Long,
                 val freightPrice: Long,
                 val productsPrice: Long,
                 val userId: Long,
                 val paymentCode: String,
                 val paymentStatus: PaymentStatus,
                 val shipmentId: Long,
                 val shipmentStatus: ShipmentStatus,
                 val shipmentInfo: ShipmentInfo)

data class OrderItemValue(val orderId: Long, val productId: Long, val productName: String, val productPrice: Long)

data class OrderItem(val id: Long,
                     val orderId: Long,
                     val productId: Long,
                     val productName: String,
                     val productPrice: Long)