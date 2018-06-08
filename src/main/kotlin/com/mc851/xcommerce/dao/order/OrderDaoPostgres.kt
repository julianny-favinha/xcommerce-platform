package com.mc851.xcommerce.dao.order

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.model.api.ShipmentInfo
import com.mc851.xcommerce.model.internal.*
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.ResultSet

class OrderDaoPostgres(private val jdbcTemplate: JdbcTemplate) : OrderDao {
    companion object {

        const val CREATE_ORDER = """INSERT INTO xcommerce.order(user_id, freight_price, products_price, payment_code, payment_status, payment_type,
                                     shipment_id, shipment_status, shipment_info, created_at) VALUES (?, ?, ?, NULL, 1, ?, NULL, 1, ?, now()) RETURNING id"""
        const val FIND_ORDER_BY_ID = """SELECT id, freight_price, products_price, user_id, payment_code, payment_status,
                                      shipment_id, shipment_status, shipment_info, created_at FROM xcommerce.order WHERE id = ?"""

        const val FIND_ORDERS_BY_STATUS = """SELECT id, freight_price, products_price, user_id, payment_code, payment_status,
                                      shipment_id, shipment_status, shipment_info, created_at FROM xcommerce.order WHERE payment_status = ? AND shipment_status = ?"""

        const val FIND_ORDERS_BY_USER = """SELECT id, freight_price, products_price, user_id, payment_code, payment_status,
                                      shipment_id, shipment_status, shipment_info, created_at FROM xcommerce.order WHERE user_id = ?"""

        const val REGISTER_PAYMENT_CODE = """UPDATE xcommerce.order SET payment_code = ? WHERE id = ? RETURNING id"""

        const val REGISTER_SHIPMENT_ID = """UPDATE xcommerce.order SET shipment_id = ? WHERE id = ? RETURNING id"""

        const val UPDATE_PAYMENT_STATUS = """UPDATE xcommerce.order SET payment_status = ? WHERE id = ? RETURNING id"""

        const val UPDATE_SHIPMENT_STATUS = """UPDATE xcommerce.order SET shipment_status = ? WHERE id = ? RETURNING id"""

    }

    private fun orderRowMapper(rs: ResultSet): Order {
        return Order(rs.getLong("id"),
                rs.getLong("freight_price"),
                rs.getLong("products_price"),
                rs.getLong("user_id"),
                rs.getString("payment_code"),
                PaymentStatus.forValue(rs.getInt("payment_status")),
                PaymentType.forValue(rs.getInt("payment_type")),
                rs.getLong("shipment_id"),
                ShipmentStatus.forValue(rs.getInt("shipment_status")),
                ShipmentInfoConverter.fromJson(rs.getString("shipment_info")),
                rs.getTimestamp("created_at"))
    }

    override fun createOrder(orderValue: OrderValue): Long? {
        return jdbcTemplate.query(CREATE_ORDER, { ps ->
            ps.setLong(1, orderValue.userId)
            ps.setLong(2, orderValue.freightPrice)
            ps.setLong(3, orderValue.productsPrice)
            ps.setInt(4, orderValue.paymentType.getId())
            ps.setString(5, ShipmentInfoConverter.toJson(orderValue.shipmentInfo))
        }, { rs, _ ->
            rs.getLong("id")
        }).firstOrNull()
    }

    override fun findOrderById(orderId: Long): Order? {
        return jdbcTemplate.query(FIND_ORDER_BY_ID, { ps ->
            ps.setLong(1, orderId)
        }, { rs, _ -> orderRowMapper(rs) }).firstOrNull()
    }

    override fun findOrderByStatus(paymentStatus: Int, shipmentStatus: Int): List<Order> {
        return jdbcTemplate.query(FIND_ORDERS_BY_STATUS, { ps ->
            ps.setInt(1, paymentStatus)
            ps.setInt(1, shipmentStatus)
        }, { rs, _ -> orderRowMapper(rs) })
    }

    override fun findOrdersByUser(userId: Long): List<Order> {
        return jdbcTemplate.query(FIND_ORDERS_BY_USER, { ps ->
            ps.setLong(1, userId)
        }, { rs, _ -> orderRowMapper(rs) })
    }

    override fun registerPayment(orderId: Long, paymentCode: String): Boolean {
        return jdbcTemplate.query(REGISTER_PAYMENT_CODE, { ps ->
            ps.setString(1, paymentCode)
            ps.setLong(2, orderId)
        }, { rs, _ -> rs.getLong("id") }).isNotEmpty()
    }

    override fun registerShipment(orderId: Long, shipmentId: Long): Boolean {
        return jdbcTemplate.query(REGISTER_SHIPMENT_ID, { ps ->
            ps.setLong(1, shipmentId)
            ps.setLong(2, orderId)
        }, { rs, _ -> rs.getLong("id") }).isNotEmpty()
    }

    override fun updatePaymentStatus(orderId: Long, paymentStatus: Int): Boolean {
        return jdbcTemplate.query(UPDATE_PAYMENT_STATUS, { ps ->
            ps.setInt(1, paymentStatus)
            ps.setLong(2, orderId)
        }, { rs, _ -> rs.getLong("id") }).isNotEmpty()
    }

    override fun updateShipmentStatus(orderId: Long, shipmentStatus: Int): Boolean {
        return jdbcTemplate.query(UPDATE_SHIPMENT_STATUS, { ps ->
            ps.setInt(1, shipmentStatus)
            ps.setLong(2, orderId)
        }, { rs, _ -> rs.getLong("id") }).isNotEmpty()
    }

}

object ShipmentInfoConverter {

    internal fun toJson(shipmentInfo: ShipmentInfo): String {
        return jacksonObjectMapper().writeValueAsString(shipmentInfo)
    }

    internal fun fromJson(rawShipmentInfo: String): ShipmentInfo {
        return jacksonObjectMapper().readValue(rawShipmentInfo)
    }
}

class OrderItemDaoPostgres(private val jdbcTemplate: JdbcTemplate) : OrderItemDao {
    companion object {
        const val CREATE_ORDER_ITEM = """INSERT INTO xcommerce.order_item(order_id, product_id, product_name, product_price) VALUES (?, ?, ?, ?) RETURNING id"""

        const val FIND_ITEMS_BY_ORDER_ID = """SELECT id, order_id, product_id, product_name, product_price FROM xcommerce.order_item WHERE order_id = ?"""
    }

    override fun createOrderItem(orderItemValue: OrderItemValue): Long? {
        return jdbcTemplate.query(CREATE_ORDER_ITEM, { ps ->
            ps.setLong(1, orderItemValue.orderId)
            ps.setLong(2, orderItemValue.productId)
            ps.setString(3, orderItemValue.productName)
            ps.setLong(4, orderItemValue.productPrice)
        }, { rs, _ ->
            rs.getLong("id")
        }).firstOrNull()
    }

    override fun findOrderItemsByOrderId(orderId: Long): List<OrderItem> {
        return jdbcTemplate.query(FIND_ITEMS_BY_ORDER_ID, { ps ->
            ps.setLong(1, orderId)
        }, { rs, _ ->
            OrderItem(rs.getLong("id"),
                    rs.getLong("order_id"),
                    rs.getLong("product_id"),
                    rs.getString("product_name"),
                    rs.getLong("product_price"))
        })
    }

}
