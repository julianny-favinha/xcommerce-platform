package com.mc851.xcommerce.dao.order

import com.mc851.xcommerce.model.internal.Order
import com.mc851.xcommerce.model.internal.OrderItem
import com.mc851.xcommerce.model.internal.OrderItemValue
import com.mc851.xcommerce.model.internal.OrderValue

interface OrderDao {

    fun createOrder(orderValue: OrderValue): Long?

    fun findOrderById(orderId: Long): Order?

    fun findOrderByStatus(paymentStatus: Int, shipmentStatus: Int): List<Order>

    fun findOrdersByUser(userId: Long): List<Order>

    fun registerPayment(orderId: Long, paymentCode: String): Boolean

    fun registerShipment(orderId: Long, shipmentId: Long): Boolean

    fun updatePaymentStatus(orderId: Long, paymentStatus: Int): Boolean

    fun updateShipmentStatus(orderId: Long, shipmentStatus: Int): Boolean

}

interface OrderItemDao {

    fun createOrderItem(orderItemValue: OrderItemValue): Long?

    fun findOrderItemsByOrderId(orderId: Long): List<OrderItem>

}