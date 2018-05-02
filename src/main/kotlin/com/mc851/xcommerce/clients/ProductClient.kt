package com.mc851.xcommerce.clients

import com.mc851.xcommerce.clients.product01.api.CategoryApi
import com.mc851.xcommerce.clients.product01.api.ProductApi
import java.util.UUID

interface ProductClient {

    fun listAllProducts(highlight: Boolean): List<ProductApi>

    fun findProductById(id: UUID): ProductApi?

    fun listAllCategory(): List<CategoryApi>

    fun findCategoryById(id: UUID): CategoryApi?
}