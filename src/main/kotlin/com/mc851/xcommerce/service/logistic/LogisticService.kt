package com.mc851.xcommerce.service.logistic

import com.mc851.xcommerce.clients.LogisticClient
import com.mc851.xcommerce.clients.logistic.api.LogisticPriceInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticRegisterInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackOutApi
import com.mc851.xcommerce.dao.logistic.LogisticDao
import com.mc851.xcommerce.model.api.Product
import com.mc851.xcommerce.model.api.ShipmentIn
import com.mc851.xcommerce.model.api.ShipmentOut
import com.mc851.xcommerce.model.internal.ShipmentIndividual

class LogisticService(val logisticClient: LogisticClient, val logisticDao: LogisticDao) {

    fun getShipmentPrice(shipment: ShipmentIndividual): ShipmentOut {

        val val_pac = LogisticPriceInApi(shipType = "PAC",
            cepDst = shipment.cepDst,
            packWeight = shipment.product.weight,
            packType = "Caixa",
            packLen = shipment.product.length.toDouble(),
            packHeight = shipment.product.height.toDouble(),
            packWidth = shipment.product.width.toDouble())

        val val_sedex = LogisticPriceInApi(shipType = "SEDEX",
            cepDst = shipment.cepDst,
            packWeight = shipment.product.weight,
            packType = "Caixa",
            packLen = shipment.product.length.toDouble(),
            packHeight = shipment.product.height.toDouble(),
            packWidth = shipment.product.width.toDouble())

        val logisticApiPac = logisticClient.calculateShipment(val_pac)
        val logisticApiSedex = logisticClient.calculateShipment(val_sedex)

        val prices = emptyMap<String, Int>().toMutableMap()

        logisticApiPac?.let {
            prices["PAC"] = it.preco
        }

        logisticApiSedex?.let {
            prices["Sedex"] = it.preco
        }

        return ShipmentOut(prices)
    }

    fun getShipmentPriceAll(shipments: ShipmentIn): ShipmentOut? {

        var totalPrices = emptyMap<String, Int>().toMutableMap()
        totalPrices["PAC"] = 0
        totalPrices["Sedex"] = 0

        // calculate total sum
        for (prod in shipments.products) {

            val shipIn = ShipmentIndividual(prod, shipments.cepDst)
            val ret = getShipmentPrice(shipIn)

            totalPrices["PAC"] = totalPrices["PAC"]!! + ret.prices["PAC"]!!
            totalPrices["Sedex"] = totalPrices["Sedex"]!! + ret.prices["Sedex"]!!
        }

        return ShipmentOut(totalPrices)
    }

    //  Register a track
    fun register(product: Product, cepDst: String): String {
        var registerIn = LogisticRegisterInApi(product.id.toInt(),
            "PAC",
            "13465-450",
            cepDst,
            product.weight.toDouble(),
            "Caixa",
            product.height.toDouble(),
            product.width.toDouble(),
            product.length.toDouble())
        val logisticApiRegister = logisticClient.register(registerIn)
                                  ?: throw IllegalStateException("Shipment registration was not possible.")
        logisticDao.insertExternalId(logisticApiRegister.codigoRastreio)
        return logisticApiRegister.codigoRastreio
    }

    //  Get order tracking given a track code
    fun getTrack(cod: String): LogisticTrackOutApi? {
        val apiKey = "b09f7e40-36a7-5c00-9d62-84112b847952"
        val order = LogisticTrackInApi(cod, apiKey)
        val logisticApiOrderTrack = logisticClient.trackOrder(order)
        return logisticApiOrderTrack
    }

}