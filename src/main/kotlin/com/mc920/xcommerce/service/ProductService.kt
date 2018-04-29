package com.mc920.xcommerce.service

import com.mc920.xcommerce.clients.ProductClient
import com.mc920.xcommerce.model.Product
import org.springframework.stereotype.Service
import java.net.URL

@Service
class ProductService(val productClient: ProductClient) {

    fun getHighlights(): List<Product> {

        val productApi = productClient.listAllProducts(true)

        return productApi.map {
            Product(id = 1, name = it.name!!, brand = it.brand!!, imageUrl = URL(it.imageUrl!!))
        }
    }
}