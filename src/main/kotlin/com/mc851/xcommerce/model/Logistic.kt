package com.mc851.xcommerce.model

data class ShipmentOut(val price: Int)

data class ShipmentIn(val cep: String, val products: List<Product>)