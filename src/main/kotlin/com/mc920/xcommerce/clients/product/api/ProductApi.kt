package com.mc920.xcommerce.clients.product.api

import java.math.BigDecimal

data class ProductApi(val id: String,
                      val name: String,
                      val description: String,
                      val price: BigDecimal,
                      val brand: String,
                      val stock: BigDecimal,
                      val categoryId: String,
                      val imageUrl: String)