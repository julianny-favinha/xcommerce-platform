package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.product01.ProductClient
import com.mc851.xcommerce.clients.product01.api.ProductApi
import com.mc851.xcommerce.dao.product.ProductDao
import com.mc851.xcommerce.model.Category
import com.mc851.xcommerce.model.Highlights
import com.mc851.xcommerce.model.Product
import com.mc851.xcommerce.model.Search
import com.sun.org.apache.xpath.internal.operations.Bool
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class ProductService(val productClient: ProductClient,
                     val productDao: ProductDao,
                     val categoryService: CategoryService) {

    fun getHighlights(): Highlights? {
        val productApi = productClient.listAllProducts(true)
        if (productApi.isEmpty()) return null

        val categoryByExternalId = categoryService.getByIds(productApi.mapNotNull { it.categoryId })

        return Highlights(highlights = productApi.map {
            val id = createRelation(it)
            val category = categoryByExternalId[it.id]
            convertProduct(id, it, category)
        })
    }

    fun getById(id: Long): Product? {
        val externalId = productDao.findById(id) ?: return null
        val product = productClient.findProductById(UUID.fromString(externalId)) ?: return null
        val category = product.categoryId?.let { categoryService.getById(product.categoryId) }

        return convertProduct(id, product, category)
    }

    fun search(text: String): Search? {
        val products = productClient.search(text)
        if (products.isEmpty()) return null
        val categoryByExternalId = categoryService.getByIds(products.mapNotNull { it.categoryId })

        return Search(result = products.map {
            val id = createRelation(it)
            val category = categoryByExternalId[it.id]
            convertProduct(id, it, category)
        })
    }

    private fun convertProduct(id: Long, productApi: ProductApi, category: Category?): Product {
        return Product(id = id,
            name = productApi.name,
            category = category?.name,
            imageUrl = productApi.imageUrl,
            brand = productApi.brand ?: "Sem Marca",
            description = productApi.description,
            height =  productApi.height,
            weight = productApi.weight,
            width = productApi.width,
            length = productApi.length,
            price = (productApi.price.multiply(BigDecimal.valueOf(100L))).toInt())
    }

    private fun createRelation(product: ProductApi): Long {
        val externalId = product.id

        return productDao.findByExternalId(externalId) ?: productDao.insertExternalId(externalId)
               ?: throw IllegalStateException("Can't create relation between ids!")
    }

    fun reserveProducts(productsByQuantity: Map<Product, Int>): Boolean {
        // para cada produto, reservar a quantidade definida.
        // se der algum erro, deve desreservar os que deram certo anteriormente
        val successfulProducts: MutableMap<Product, Int> = emptyMap<Product, Int>().toMutableMap()

        for (entry in productsByQuantity) {
            val externalId = productDao.findById(entry.key.id)
            val result = productClient.reserve(UUID.fromString(externalId), entry.value)

            if (!result) {
                break
            }

            successfulProducts[entry.key] = entry.value
        }

        if (productsByQuantity == successfulProducts) {
           return true
        }

        // desreservar
        releaseProducts(successfulProducts)

        return false
    }

    fun releaseProducts(productsByQuantity: Map<Product, Int>): Boolean {
        productsByQuantity.map {
            val externalId = productDao.findById(it.key.id)
            productClient.release(UUID.fromString(externalId), it.value)
        }

        return true
    }
}