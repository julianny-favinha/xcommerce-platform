package com.mc851.xcommerce.dao.order

import com.mc851.xcommerce.model.internal.Order
import com.mc851.xcommerce.model.internal.OrderItem
import com.mc851.xcommerce.model.internal.OrderItemValue
import com.mc851.xcommerce.model.internal.OrderValue

interface OrderDao {

    fun createOrder(orderValue: OrderValue): Long

    fun findOrderById(orderId: Long): Order

}

interface OrderItemDao {

    fun createOrderItem(orderItemValu: OrderItemValue): Long

    fun findOrderItemsByOrderId(orderId: Long): List<OrderItem>

}