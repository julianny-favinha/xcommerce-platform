package com.mc920.xcommerce.model

data class Product(val id: Int, val name: String, val brand: String, val imageUrl: String?)

data class Highlights(val highlights: List<Product>)