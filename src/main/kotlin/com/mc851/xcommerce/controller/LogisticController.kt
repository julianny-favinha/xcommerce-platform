package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.ShipmentIn
import com.mc851.xcommerce.model.ShipmentOut
import com.mc851.xcommerce.service.LogisticService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("logistic")
class LogisticController {

    @Autowired
    lateinit var logisticService: LogisticService

    @PostMapping("/calculate")
    fun calculateShipmentPrice(@RequestBody request: ShipmentIn): ResponseEntity<ShipmentOut> {
        val price = logisticService.getShipmentPrice(request.products, request.cep)
        return handleResponse(price)
    }


}
