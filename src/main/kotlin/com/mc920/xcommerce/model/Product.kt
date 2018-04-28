package com.mc920.xcommerce.model

import java.net.URL

data class Product(val id: Int,
                   val name: String,
                   val brand: String,
                   val imageUrl: URL)