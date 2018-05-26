package com.mc851.xcommerce.model.api

data class ShipmentOut(val prices: Map<String, Int>)

data class ShipmentIn(val products: List<Product>, val cepDst: String)


