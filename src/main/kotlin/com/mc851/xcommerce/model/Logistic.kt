package com.mc851.xcommerce.model

data class ShipmentOut(val prices: Map<String, Int>)

data class ShipmentIn(val products: List<Product>, val cepFrom: String, val cepDst: String)

data class ShipmentInIndividual(val product: Product, val cepFrom: String, val cepDst: String)

data class OrderIn(val product: Product, val cepDst: String)

