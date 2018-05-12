package com.mc851.xcommerce.model

data class Product(val id: Int, val name: String, val brand: String, val price: Int, val category: String, val description: String, val imageUrl: String?)

data class Highlights(val highlights: List<Product>)