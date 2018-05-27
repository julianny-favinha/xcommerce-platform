package com.mc851.xcommerce.model.internal

data class OrderValue(val freightPrice: Long, val productsPrice: Long, val userId: Long)

data class Order(val id: Long,
                 val freightPrice: Long,
                 val productsPrice: Long,
                 val userId: Long,
                 val paymentStatus: PaymentStatus,
                 val shipmentStatus: ShipmentStatus)

data class OrderItemValue(val orderId: Long, val productId: Long, val productName: String, val productPrice: Long)

data class OrderItem(val id: Long,
                     val orderId: Long,
                     val productId: Long,
                     val productName: String,
                     val productPrice: Long)