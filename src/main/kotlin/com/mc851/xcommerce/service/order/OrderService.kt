package com.mc851.xcommerce.service.order

import com.mc851.xcommerce.dao.order.OrderDao
import com.mc851.xcommerce.dao.order.OrderItemDao
import com.mc851.xcommerce.model.api.*
import com.mc851.xcommerce.model.internal.*
import com.mc851.xcommerce.model.internal.Order
import com.mc851.xcommerce.service.logistic.LogisticService
import com.mc851.xcommerce.service.product.ProductService
import com.mc851.xcommerce.service.user.UserService
import mu.KotlinLogging
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class OrderService(private val logisticService: LogisticService,
                   private val orderDao: OrderDao,
                   private val orderItemDao: OrderItemDao,
                   private val productService: ProductService) {

    private val log = KotlinLogging.logger { }

    fun registerOrder(productsByQuantity: Map<Product, Long>, userId: Long, shipmentInfo: ShipmentInfo, paymentType: PaymentType): Long? {
        val price = productsByQuantity.map {
            it.key.price * it.value
        }.sum()

        val products = productsByQuantity.map {
            it.key
        }

        val freightPrice =
                logisticService.getShipmentPriceAll(ShipmentIn(products, shipmentInfo.cepDst), shipmentInfo.shipmentType)

        val orderValue =
                OrderValue(freightPrice = freightPrice, productsPrice = price, userId = userId, shipmentInfo = shipmentInfo, paymentType = paymentType)

        val orderId = orderDao.createOrder(orderValue) ?: TODO("deal with it")

        registerOrderItem(productsByQuantity, orderId)
        return orderId
    }

    fun retrieveOrder(orderId: Long): Order? {
        return orderDao.findOrderById(orderId) ?: TODO("deal with it")
    }

    fun registerPayment(orderId: Long, paymentCode: String) {
        orderDao.registerPayment(orderId, paymentCode)
    }

    fun updatePaymentStatus(orderId: Long, afterStatus: PaymentStatus) {
        val result = orderDao.updatePaymentStatus(orderId, afterStatus.getId())
        log.info { "Result of update $result" }
    }

    fun registerShipment(orderId: Long, shipmentId: Long) {
        orderDao.registerShipment(orderId, shipmentId)
    }

    fun updateShipmentStatus(orderId: Long, afterStatus: ShipmentStatus) {
        orderDao.updateShipmentStatus(orderId, afterStatus.getId())
    }

    fun cancelOrder(orderId: Long): Boolean {
        return orderDao.updatePaymentStatus(orderId, PaymentStatus.CANCELED.getId())
    }

    fun findOrdersByStatus(paymentStatus: PaymentStatus, shipmentStatus: ShipmentStatus): List<Order> {
        return orderDao.findOrderByStatus(paymentStatus.getId(), shipmentStatus.getId())
    }

    fun findOrderItems(orderId: Long): List<OrderItem> {
        return orderItemDao.findOrderItemsByOrderId(orderId)
    }

    fun findUserOrders(userId: Long): Orders {
        val orders = orderDao.findOrdersByUser(userId)


        val productsByOrder = orders.map { it.id to orderItemDao.findOrderItemsByOrderId(it.id) }.map {
            it.first to it.second.map { (productService.getById(it.productId) ?: throw IllegalStateException("Product not found!")) to it.quantity }.map { CartItem( it.first, it.second ) }
        }.toMap()

        return Orders(orders.map {
            com.mc851.xcommerce.model.api.Order(productsByOrder[it.id]!!,
                    it.id,
                    it.freightPrice + it.productsPrice,
                    it.freightPrice,
                    productsByOrder[it.id]!!.size.toLong(),
                    it.shipmentInfo,
                    it.shipmentStatus,
                    it.paymentStatus,
                    it.paymentType,
                    it.paymentCode,

                    it.createdAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        })
    }

    private fun registerOrderItem(products: Map<Product, Long>, orderId: Long) {

        products.map {
            OrderItemValue(orderId, it.key.id, it.key.name, it.key.price.toLong(), it.value)
        }.forEach {
            orderItemDao.createOrderItem(it)
        }

    }
}