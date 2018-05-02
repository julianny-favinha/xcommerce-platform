package com.mc920.xcommerce.clients.product01.api

import java.math.BigDecimal

data class ProductApi(val id: String?,
                      val status: String?,
                      val additionalInfo: Map<String, String>?,
                      val name: String?,
                      val description: String?,
                      val price: BigDecimal?,
                      val stock: BigDecimal?,
                      val brand: String?,
                      val highlight: Boolean?,
                      val categoryId: String?,
                      val imageUrl: String?,
                      val tags: List<String>?,
                      val createdAt: Long?,
                      val updatedAt: Long?)
