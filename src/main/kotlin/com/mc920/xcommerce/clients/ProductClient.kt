package com.mc920.xcommerce.clients

import com.mc920.xcommerce.clients.product01.api.ProductApi
import java.util.UUID

interface ProductClient {

    fun listAllProducts(highlight: Boolean): List<ProductApi>

    fun findProductById(id: UUID): ProductApi?
}