package com.mc851.xcommerce.jobs

import com.mc851.xcommerce.model.internal.PaymentStatus
import com.mc851.xcommerce.model.internal.ShipmentStatus
import com.mc851.xcommerce.service.logistic.LogisticService
import com.mc851.xcommerce.service.order.OrderService
import com.mc851.xcommerce.service.payment.PaymentService
import com.mc851.xcommerce.service.product.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class Jobs {

    @Autowired
    lateinit var orderService: OrderService

    @Autowired
    lateinit var productService: ProductService

    @Autowired
    lateinit var logisticService: LogisticService

    @Autowired
    lateinit var paymentService: PaymentService

    @Scheduled(fixedRate = 30000)
    fun verifyBoletoPamentJob() {
        val orders = orderService.findOrdersByStatus(PaymentStatus.PENDING, ShipmentStatus.NOT_STARTED)
        if (orders.isEmpty()) {
            return
        }

        orders.forEach { order ->
            order.paymentCode?.let {
            val paymentStatus = paymentService.getPaymentStatus(order.paymentCode)
            paymentStatus?.let {
                orderService.updatePaymentStatus(order.id, it)
            }
            }
        }
    }

    @Scheduled(fixedRate = 50000)
    fun preShipmentJob() {
        val orders = orderService.findOrdersByStatus(PaymentStatus.OK, ShipmentStatus.NOT_STARTED)
        if (orders.isEmpty()) {
            return
        }

        orders.forEach { order ->

            val orderItems = orderService.findOrderItems(order.id)

            val products = orderItems.map { it.productId }.mapNotNull { productService.getById(it) }

            val shipmentId =
                logisticService.registerAll(products, order.shipmentInfo.cepDst, order.shipmentInfo.shipmentType)

            shipmentId?.let {
                orderService.registerShipment(order.id, it)
                orderService.updateShipmentStatus(order.id, ShipmentStatus.PREPARING_FOR_SHIPMENT)
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    fun releaseJob() {
        System.out.println("Time in task: " + (System.currentTimeMillis() / 1000))
    }

}