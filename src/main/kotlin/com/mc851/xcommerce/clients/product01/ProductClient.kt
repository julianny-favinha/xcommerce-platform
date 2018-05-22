package com.mc851.xcommerce.clients.product01

import com.mc851.xcommerce.clients.product01.api.CategoryApi
import com.mc851.xcommerce.clients.product01.api.ProductApi
import java.util.UUID

interface ProductClient {

    fun listAllProducts(highlight: Boolean): List<ProductApi>

    fun findProductById(id: UUID): ProductApi?

    fun listAllCategories(): List<CategoryApi>

    fun findCategoryById(id: UUID): CategoryApi?

    fun search(text: String): List<ProductApi>

    fun reserve(id: UUID, quantity: Int): Boolean

    fun release(id: UUID, quantity: Int): Boolean
}