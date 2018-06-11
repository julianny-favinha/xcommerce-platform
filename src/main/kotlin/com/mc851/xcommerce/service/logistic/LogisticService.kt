package com.mc851.xcommerce.service.logistic

import com.mc851.xcommerce.clients.LogisticClient
import com.mc851.xcommerce.clients.logistic.api.LogisticInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticRegisterInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackOutApi
import com.mc851.xcommerce.dao.logistic.LogisticDao
import com.mc851.xcommerce.model.api.Product
import com.mc851.xcommerce.model.api.ShipmentIn
import com.mc851.xcommerce.model.api.ShipmentOut
import com.mc851.xcommerce.model.internal.ShipmentIndividual
import com.mc851.xcommerce.model.internal.ShipmentStatus
import com.mc851.xcommerce.model.internal.ShipmentType
import kotlin.math.max

class LogisticService(val logisticClient: LogisticClient, val logisticDao: LogisticDao) {

    fun getShipmentPrice(shipment: ShipmentIndividual, shipmentType: ShipmentType): Long {

        val logisticIn = LogisticInApi(shipType = shipmentType.name,
            cepDst = shipment.cepDst,
            packWeight = shipment.product.weight,
            packType = "Caixa",
            packLen = shipment.product.length.toDouble(),
            packHeight = shipment.product.height.toDouble(),
            packWidth = shipment.product.width.toDouble())

        return logisticClient.calculateShipment(logisticIn)?.preco?.toLong()
               ?: throw IllegalStateException("Shipment price not found")
    }

    fun getShipment(shipment: ShipmentIndividual): ShipmentOut {

        val val_pac = LogisticInApi(shipType = "PAC",
            cepDst = shipment.cepDst,
            packWeight = shipment.product.weight,
            packType = "Caixa",
            packLen = shipment.product.length.toDouble(),
            packHeight = shipment.product.height.toDouble(),
            packWidth = shipment.product.width.toDouble())

        val val_sedex = LogisticInApi(shipType = "SEDEX",
            cepDst = shipment.cepDst,
            packWeight = shipment.product.weight,
            packType = "Caixa",
            packLen = shipment.product.length.toDouble(),
            packHeight = shipment.product.height.toDouble(),
            packWidth = shipment.product.width.toDouble())

        val logisticApiPac = logisticClient.calculateShipment(val_pac)
        val logisticApiSedex = logisticClient.calculateShipment(val_sedex)

        val prices = emptyMap<String, Int>().toMutableMap()
        val prazos = emptyMap<String, Int>().toMutableMap()

        logisticApiPac?.let {
            prices["PAC"] = it.preco
            prazos["PAC"] = it.prazo
        }

        logisticApiSedex?.let {
            prices["Sedex"] = it.preco
            prazos["Sedex"] = it.prazo
        }

        return ShipmentOut(prices, prazos)
    }

    fun getShipmentPriceAll(shipments: ShipmentIn, shipmentType: ShipmentType): Long {
        return shipments.products.map {
            val shipIn = ShipmentIndividual(it, shipments.cepDst)
            getShipmentPrice(shipIn, shipmentType)
        }.sum()
    }

    fun getShipmentAll(shipments: ShipmentIn): ShipmentOut? {

        var totalPrices = emptyMap<String, Int>().toMutableMap()
        totalPrices["PAC"] = 0
        totalPrices["Sedex"] = 0

        var prazos = emptyMap<String, Int>().toMutableMap()
        prazos["PAC"] = 0
        prazos["Sedex"] = 0

        // calculate total sum
        for (prod in shipments.products) {

            val shipIn = ShipmentIndividual(prod, shipments.cepDst)
            val ret = getShipment(shipIn)

            totalPrices["PAC"] = totalPrices["PAC"]!! + ret.prices["PAC"]!!
            totalPrices["Sedex"] = totalPrices["Sedex"]!! + ret.prices["Sedex"]!!

            prazos["PAC"] = max(prazos["PAC"]!! , ret.prazos["PAC"]!!)
            prazos["Sedex"] = max(prazos["Sedex"]!! , ret.prazos["Sedex"]!!)
        }

        return ShipmentOut(totalPrices, prazos)
    }

    //  Register a track
    fun register(product: Product, cepDst: String, shipmentType: ShipmentType): String? {
        val registerIn = LogisticRegisterInApi(product.id.toInt(),
            shipmentType.name,
            cepDst,
            product.weight.toDouble(),
            "Caixa",
            product.height.toDouble(),
            product.width.toDouble(),
            product.length.toDouble())
        val logisticApiRegister = logisticClient.register(registerIn)
        return logisticApiRegister?.codigoRastreio
    }

    fun registerAll(products: List<Product>, cepDst: String, shipmentType: ShipmentType): Long? {
        val externalIds = products.mapNotNull {
            register(it, cepDst, shipmentType)
        }

        if (externalIds.size != products.size) {
            return null
        }

        return logisticDao.insertExternalIds(externalIds) ?: throw IllegalStateException("Couldn't insert external Ids")
    }

    fun trackOrder(id: Long): ShipmentStatus {
        val trackStatus = logisticDao.findById(id).mapNotNull {
            getTrack(it)
        }.map { it.historicoRastreio.sortedWith(compareByDescending { it.hora }).first() }.map { it.mensagem }

        if (trackStatus.all { it == "Entregue" }) {
            return ShipmentStatus.DELIVERED
        } else if (trackStatus.all { it == "Em Transito" || it == "Saiu para Entrega" }) {
            return ShipmentStatus.SHIPPED
        }

        return ShipmentStatus.PREPARING_FOR_SHIPMENT

    }

    //  Get order tracking given a track code
    fun getTrack(cod: String): LogisticTrackOutApi? {
        val apiKey = "b09f7e40-36a7-5c00-9d62-84112b847952"
        val order = LogisticTrackInApi(cod, apiKey)
        val logisticApiOrderTrack = logisticClient.trackOrder(order)
        return logisticApiOrderTrack
    }

}