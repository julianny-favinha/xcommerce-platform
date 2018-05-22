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
     * TODO: efetuar requisição de fato
     * **/
    @GetMapping("/calculate/{typeShip}/{cepFrom}/{cepDst}/{weight}/{typePack}/{length}/{height}/{width}")
    fun calculateShipmentPrice(@PathVariable(name = "typeShip", required=true) typeShip: String,
                               @PathVariable(name = "cepFrom", required=true) cepFrom: String,
                               @PathVariable(name = "cepDst", required=true) cepDst: String,
                               @PathVariable(name = "weight", required=true) weight: Long,
                               @PathVariable(name = "typePack", required=true) typePack: String,
                               @PathVariable(name = "length", required=true) length: Long,
                               @PathVariable(name = "height", required=true) height: Long,
                               @PathVariable(name = "width", required=true) width: Long
                               ): ResponseEntity<ShipmentOut> {

        val prod = ShipmentIn(typeShip, cepFrom, cepDst, weight, typePack, length, height, width)
        val price = logisticService.getShipmentPrice(prod)
        return handleResponse(price)
    }

    /**
     * Register a product
     * @param request pedidos a serem inseridos
     * TODO: inserir no banco
     * **/
    @PostMapping("/register")
    fun Register(@RequestBody request: OrderIn): ResponseEntity<LogisticRegisterOutApi> {
        val reg = logisticService.register(request.product, request.cepDst)
        return handleResponse(reg)
    }

    /**
     * Track an order
     * @param request Order track code
     * TODO: carregar histórico
     * **/
    @GetMapping("/track/{cod}")
    fun Register(@PathVariable(name = "cod", required=true) cod: String): ResponseEntity<LogisticTrackOutApi> {
        val tracking = logisticService.getTrack(cod)
        return handleResponse(tracking)
    }


}
