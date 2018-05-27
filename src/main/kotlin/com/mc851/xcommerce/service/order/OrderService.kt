package com.mc851.xcommerce.service.order

import com.mc851.xcommerce.dao.order.OrderDao
import com.mc851.xcommerce.dao.order.OrderItemDao
import com.mc851.xcommerce.model.api.Product
import com.mc851.xcommerce.model.api.ShipmentIn
import com.mc851.xcommerce.model.api.ShipmentInfo
import com.mc851.xcommerce.model.internal.Order
import com.mc851.xcommerce.model.internal.OrderItemValue
import com.mc851.xcommerce.model.internal.OrderValue
import com.mc851.xcommerce.model.internal.PaymentStatus
import com.mc851.xcommerce.model.internal.ShipmentStatus
import com.mc851.xcommerce.service.logistic.LogisticService

class OrderService(private val logisticService: LogisticService,
                   private val orderDao: OrderDao,
                   private val orderItemDao: OrderItemDao) {

    fun registerOrder(products: List<Product>, userId: Long, shipmentInfo: ShipmentInfo): Long {
        val price = products.map {
            it.price
        }.sum().toLong()

        val freightPrice =
            logisticService.getShipmentPriceAll(ShipmentIn(products, shipmentInfo.cepDst), shipmentInfo.shipmentType)

        val orderValue = OrderValue(freightPrice = freightPrice, productsPrice = price, userId = userId)

        val orderId = orderDao.createOrder(orderValue)

        registerOrderItem(products, orderId)
        return orderId
    }

    fun retrieveOrder(orderId: Long): Order {
        return orderDao.findOrderById(orderId)
    }

    fun registerPayment(orderId: Long, paymentCode: String) {
        orderDao.registerPayment(orderId, paymentCode)
    }

    fun updatePaymentStatus(orderId: Long, afterStatus: PaymentStatus) {
        orderDao.updatePaymentStatus(orderId, afterStatus.getId().toLong())
    }

    fun registerShipment(orderId: Long, shipmentId: Long) {
        orderDao.registerShipment(orderId, shipmentId)
    }

    fun updateShipmentStatus(orderId: Long, afterStatus: ShipmentStatus) {
        orderDao.updateShipmentStatus(orderId, afterStatus.getId().toLong())
    }

    fun cancelOrder(orderId: Long): Boolean {
        orderItemDao.findOrderItemsByOrderId(orderId).map {
            orderItemDao.cancelOrderItem(it.id)
        }
        return orderDao.cancelOrder(orderId)
    }

    private fun registerOrderItem(products: List<Product>, orderId: Long) {

        products.map {
            OrderItemValue(orderId, it.id, it.name, it.price.toLong())
        }.forEach {
            orderItemDao.createOrderItem(it)
        }

    }
}