package com.mc920.xcommerce.service

import com.mc920.xcommerce.clients.ProductClient
import com.mc920.xcommerce.clients.product01.api.ProductApi
import com.mc920.xcommerce.dao.ProductDao
import com.mc920.xcommerce.model.Highlights
import com.mc920.xcommerce.model.Product
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(val productClient: ProductClient, val productDao: ProductDao) {

    fun getHighlights(): Highlights? {

        val productApi = productClient.listAllProducts(true)

        if(productApi.isEmpty()) return null

        return Highlights(highlights = productApi.map { createRelation(it) to it }.map { (id, product) ->
            Product(id = id.toInt(), name = product.name!!, brand = product.brand!!, imageUrl = product.imageUrl)
        })
    }

    fun getById(id: Long): Product? {
        val externalId = productDao.findById(id) ?: return null
        val product = productClient.findProductById(UUID.fromString(externalId)) ?: return null

        // return Product(id = id.toInt(), name = product.name!!, brand = product.brand!!, imageUrl = product.imageUrl)
    }

    private fun createRelation(product: ProductApi): Long {
        val externalId = product.id ?: throw IllegalStateException("External Id must exist!")

        return productDao.findByExternalId(externalId) ?: productDao.insertExternalId(externalId)
               ?: throw IllegalStateException("Can't create relation between ids!")
    }
}