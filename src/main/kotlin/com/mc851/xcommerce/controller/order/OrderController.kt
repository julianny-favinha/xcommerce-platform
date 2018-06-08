package com.mc851.xcommerce.controller.order

import com.mc851.xcommerce.controller.utils.handleResponse
import com.mc851.xcommerce.filters.RequestContext
import com.mc851.xcommerce.model.api.Orders
import com.mc851.xcommerce.model.internal.Order
import com.mc851.xcommerce.service.order.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("order")
class OrderController {

    @Autowired
    lateinit var orderService: OrderService

    @GetMapping("")
    fun getOrders(@ModelAttribute(RequestContext.CONTEXT) context: RequestContext): ResponseEntity<Orders> {
        val orders = orderService.findUserOrders(context.userId!!)
        return handleResponse(orders)
    }
}