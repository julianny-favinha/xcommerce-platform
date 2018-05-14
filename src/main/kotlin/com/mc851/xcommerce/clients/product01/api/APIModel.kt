package com.mc851.xcommerce.clients.product01.api

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProductApi(val id: String,
                      val name: String,
                      val description: String,
                      val price: BigDecimal,
                      val stock: BigDecimal,
                      val brand: String,
                      val categoryId: String,
                      val imageUrl: String?,
                      val createdAt: Long?,
                      val updatedAt: Long?)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategoryApi(val id: String,
                       val name: String,
                       val description: String?,
                       val parentId: String?,
                       val additionalInfo: Map<String, String>?,
                       val status: String?,
                       val createdAt: Long?,
                       val updatedAt: Long?)