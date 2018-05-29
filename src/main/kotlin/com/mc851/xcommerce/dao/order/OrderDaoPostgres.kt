package com.mc851.xcommerce.dao.order

import com.mc851.xcommerce.model.internal.Order
import com.mc851.xcommerce.model.internal.OrderItem
import com.mc851.xcommerce.model.internal.OrderItemValue
import com.mc851.xcommerce.model.internal.OrderValue
import org.springframework.jdbc.core.JdbcTemplate

class Queries {
    companion object {
        const val INSERT_EXTERNAL_ID = """INSERT INTO xcommerce.product_relation(external_id) VALUES (?) RETURNING id"""
        const val FIND_BY_ID = """SELECT external_id FROM xcommerce.product_relation WHERE id = ?"""
        const val FIND_BY_EXTERNAL_ID = """SELECT id FROM xcommerce.product_relation WHERE external_id = ?"""
    }
}

class OrderDaoPostgres(jdbcTemplate: JdbcTemplate) : OrderDao {
    override fun createOrder(orderValue: OrderValue): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findOrderById(orderId: Long): Order {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findOrderByStatus(paymentStatus: Int, shipmentStatus: Int): List<Order> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancelOrder(orderId: Long): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerPayment(orderId: Long, paymentCode: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerShipment(orderId: Long, shipmentId: Long): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updatePaymentStatus(orderId: Long, paymentStatus: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateShipmentStatus(orderId: Long, shipmentStatus: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class OrderItemDaoPostgres(jdbcTemplate: JdbcTemplate) : OrderItemDao {
    override fun createOrderItem(orderItemValu: OrderItemValue): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findOrderItemsByOrderId(orderId: Long): List<OrderItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancelOrderItem(orderItemId: Long): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
