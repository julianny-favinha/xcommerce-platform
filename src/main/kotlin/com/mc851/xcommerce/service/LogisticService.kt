package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.LogisticClient
import com.mc851.xcommerce.clients.logistic.api.LogisticPriceInApi
import com.mc851.xcommerce.dao.logistic.LogisticDao
import com.mc851.xcommerce.model.Product
import com.mc851.xcommerce.model.ShipmentOut
import org.springframework.stereotype.Service

@Service
class LogisticService(val logisticClient: LogisticClient,
                     val logisticDao: LogisticDao) {

    fun getShipmentPrice(products: List<Product>, cepDst: String): ShipmentOut? {

        products.map{ product: Product ->  product.}

        val values = LogisticPriceInApi()

        val logisticApi = logisticClient.calculateShipment(values)

        return ShipmentOut((logisticApi?.price ?: 0 * 100).toInt())
    }


}