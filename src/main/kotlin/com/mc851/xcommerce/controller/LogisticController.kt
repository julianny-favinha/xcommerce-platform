package com.mc851.xcommerce.controller

import com.mc851.xcommerce.clients.logistic.api.LogisticTrackInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackOutApi
import com.mc851.xcommerce.clients.logistic.api.LogisticRegisterInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticRegisterOutApi
import com.mc851.xcommerce.model.ShipmentIn
import com.mc851.xcommerce.model.ShipmentOut
import com.mc851.xcommerce.model.OrderIn
import com.mc851.xcommerce.model.Product
import com.mc851.xcommerce.service.LogisticService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("logistic")
class LogisticController {

    @Autowired
    lateinit var logisticService: LogisticService

    /**
     * Calculate the price of shipment by given item
     * @param request Product to be calculated
     * **/
    @PostMapping("/calculate")
    fun calculateShipmentPrice(@RequestBody request:ShipmentIn): ResponseEntity<ShipmentOut> {
        val prod = ShipmentIn(request.typeShip, request.cepFrom, request.cepDst, request.weight, request.typePack, request.length, request.height, request.width)
        val price = logisticService.getShipmentPrice(prod)
        return handleResponse(price)
    }


    /**
     * Register a product shipment
     * @param request Product to be inserted
     * **/
    @PostMapping("/register")
    fun Register(@RequestBody request: OrderIn): ResponseEntity<LogisticRegisterOutApi> {
        val reg = logisticService.register(request.product, request.cepDst)
        return handleResponse(reg)
    }


    /**
     * Track an order
     * @param request Order track code
     * **/
    @GetMapping("/track/{cod}")
    fun Register(@PathVariable(name = "cod", required=true) cod: String): ResponseEntity<LogisticTrackOutApi> {
        val tracking = logisticService.getTrack(cod)
        return handleResponse(tracking)
    }


}
