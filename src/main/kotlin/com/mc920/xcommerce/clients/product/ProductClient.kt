package com.mc920.xcommerce.clients.product

import com.mc920.xcommerce.clients.product.api.ProductApi
import java.util.UUID

interface ProductClient {

    fun listAllProducts(): List<ProductApi>

    fun findProductById(id: UUID): ProductApi?
}