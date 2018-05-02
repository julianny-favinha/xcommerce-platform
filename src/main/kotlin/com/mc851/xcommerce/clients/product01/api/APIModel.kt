package com.mc851.xcommerce.clients.product01.api

import java.math.BigDecimal

data class ProductApi(val id: String,
                      val status: String?,
                      val additionalInfo: Map<String, String>?,
                      val name: String?,
                      val description: String?,
                      val price: BigDecimal?,
                      val stock: BigDecimal?,
                      val brand: String?,
                      val highlight: Boolean?,
                      val categoryId: String,
                      val imageUrl: String?,
                      val tags: List<String>?,
                      val createdAt: Long?,
                      val updatedAt: Long?)

data class CategoryApi(val id: String,
                       val name: String,
                       val description: String?,
                       val parentId: String?,
                       val additionalInfo: Map<String, String>?,
                       val status: String?,
                       val createdAt: Long?,
                       val updatedAt: Long?)