package com.mc851.xcommerce.clients

import com.mc851.xcommerce.clients.product01.api.CategoryApi
import com.mc851.xcommerce.clients.product01.api.ProductApi
import java.util.UUID

interface LogisticClient {

    fun calculateShipment(shipType: String,
                          cepFrom: String,
                          cepDst: String,
                          packWeight: Int,
                          packType: String,
                          packLen: Double,
                          packHeight: Double,
                          packWidth: Double): Integer

}