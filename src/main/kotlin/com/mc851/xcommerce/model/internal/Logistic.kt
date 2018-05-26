package com.mc851.xcommerce.model.internal

import com.mc851.xcommerce.model.api.Product

data class ShipmentIndividual(val product: Product, val cepDst: String)
