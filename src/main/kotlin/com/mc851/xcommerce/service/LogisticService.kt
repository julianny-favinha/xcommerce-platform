package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.ProductClient
import com.mc851.xcommerce.clients.product01.api.ProductApi
import com.mc851.xcommerce.dao.product.ProductDao
import com.mc851.xcommerce.model.Category
import com.mc851.xcommerce.model.Highlights
import com.mc851.xcommerce.model.Product
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LogisticService(val productClient: ProductClient,
                     val productDao: ProductDao,
                     val categoryService: CategoryService) {

    fun getHighlights(): Highlights? {
        val productApi = productClient.listAllProducts(true)
        if (productApi.isEmpty()) return null

        val categoryByExternalId = categoryService.getByIds(productApi.map { it.categoryId })

        return Highlights(highlights = productApi.map {
            val id = createRelation(it)
            val category = categoryByExternalId[it.id]!!
            convertProduct(id, it, category)
        })
    }

    fun getById(id: Long): Product? {
        val externalId = productDao.findById(id) ?: return null
        val product = productClient.findProductById(UUID.fromString(externalId)) ?: return null
        val category = categoryService.getById(product.categoryId)

        return convertProduct(id, product, category)
    }

    private fun convertProduct(id: Long, productApi: ProductApi, category: Category?): Product {
        return Product(id = id.toInt(),
                name = productApi.name ?: throw IllegalStateException("Product doesn't have name"),
                category = category?.name ?: "",
                imageUrl = productApi.imageUrl,
                brand = productApi.brand ?: "",
                description = productApi.description ?: "",
                price = productApi.price?.toInt() ?: throw IllegalStateException("Product doesn't have price!"))
    }

    private fun createRelation(product: ProductApi): Long {
        val externalId = product.id

        return productDao.findByExternalId(externalId) ?: productDao.insertExternalId(externalId)
        ?: throw IllegalStateException("Can't create relation between ids!")
    }
}