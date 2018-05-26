package com.mc851.xcommerce.clients.product01.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductApi(val id: String,
                      val name: String,
                      val description: String,
                      val price: BigDecimal,
                      val stock: BigDecimal,
                      val brand: String?,
                      val imageUrl: String?,
                      val categoryId: String?,
                      val weight: Long,
                      val length: Long,
                      val width: Long,
                      val height: Long)

@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryApi(val id: String, val name: String, val description: String?)