package com.mc920.xcommerce.service

import com.mc920.xcommerce.clients.ProductClient
import com.mc920.xcommerce.dao.ProductDao
import com.mc920.xcommerce.model.Product
import org.springframework.stereotype.Service
import java.net.URL

@Service
class ProductService(val productClient: ProductClient, val productDao: ProductDao) {

    fun getHighlights(): List<Product> {

        val productApi = productClient.listAllProducts(true)

        return productApi.map { productDao.insertExternalId(it.id!!) to it }
            .filter { (id, _) -> id != null }
            .map { (id, product) ->
                Product(id = id!!.toInt(),
                    name = product.name!!,
                    brand = product.brand!!,
                    imageUrl = URL(product.imageUrl!!))
            }
    }
}