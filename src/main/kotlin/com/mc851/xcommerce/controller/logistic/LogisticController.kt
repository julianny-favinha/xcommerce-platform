package com.mc851.xcommerce.controller.logistic

import com.mc851.xcommerce.controller.utils.handleResponse
import com.mc851.xcommerce.model.api.ShipmentIn
import com.mc851.xcommerce.model.api.ShipmentOut
import com.mc851.xcommerce.service.logistic.LogisticService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("logistic")
class LogisticController {

    @Autowired
    lateinit var logisticService: LogisticService

    @PostMapping("/calculate")
    fun calculateShipmentPrice(@RequestBody request: ShipmentIn): ResponseEntity<ShipmentOut> {
        val price = logisticService.getShipmentPriceAll(request)
        return handleResponse(price)
    }

}
