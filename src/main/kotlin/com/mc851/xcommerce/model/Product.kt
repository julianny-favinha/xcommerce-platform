package com.mc851.xcommerce.model

data class Product(val id: Long, val name: String, val brand: String, val price: Int, val weight: Long, val length: Long, val width: Long,
                   val height: Long, val category: String?, val description: String, val imageUrl: String?)

data class Highlights(val highlights: List<Product>)

data class Search(val result: List<Product>)