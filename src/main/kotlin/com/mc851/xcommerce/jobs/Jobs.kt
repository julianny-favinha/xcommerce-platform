package com.mc851.xcommerce.jobs

import com.mc851.xcommerce.model.internal.PaymentStatus
import com.mc851.xcommerce.model.internal.ShipmentStatus
import com.mc851.xcommerce.service.logistic.LogisticService
import com.mc851.xcommerce.service.order.OrderService
import com.mc851.xcommerce.service.payment.PaymentService
import com.mc851.xcommerce.service.product.ProductService
import mu.KotlinLogging
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

    private val log = KotlinLogging.logger {}

    @Scheduled(fixedRate = 7000)
    fun verifyBoletoPamentJob() {
        val orders = orderService.findOrdersByStatus(PaymentStatus.PENDING, ShipmentStatus.NOT_STARTED)
        if (orders.isEmpty()) {
            return
        }


        orders.forEach { order ->
            order.paymentCode?.let {
                val paymentStatus = paymentService.getPaymentStatus(order.paymentCode)
                paymentStatus?.let {
                    log.info { "paymentStatus exsite $paymentStatus" }
                    orderService.updatePaymentStatus(order.id, it)
                }
            }
        }
    }

    @Scheduled(fixedRate = 18000)
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

    @Scheduled(fixedRate = 11000)
    fun shipmentJob() {
        val orders = orderService.findOrdersByStatus(PaymentStatus.OK, ShipmentStatus.PREPARING_FOR_SHIPMENT) + orderService.findOrdersByStatus(PaymentStatus.OK, ShipmentStatus.SHIPPED)
        if (orders.isEmpty()) {
            return
        }

        orders.forEach { order ->

            val trackOrder = logisticService.trackOrder(order.id)
            if(order.shipmentStatus != trackOrder){
                orderService.updateShipmentStatus(order.id, trackOrder)
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    fun releaseJob() {
        productService.releaseExpire()
    }

}