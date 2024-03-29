package com.mc851.xcommerce.service.product

import com.mc851.xcommerce.clients.ProductClient
import com.mc851.xcommerce.clients.product01.api.ProductApi
import com.mc851.xcommerce.dao.product.ProductDao
import com.mc851.xcommerce.dao.product.ProductExpirationDao
import com.mc851.xcommerce.model.api.Category
import com.mc851.xcommerce.model.api.Highlights
import com.mc851.xcommerce.model.api.Product
import com.mc851.xcommerce.model.api.Search
import mu.KotlinLogging
import java.math.BigDecimal
import java.util.UUID

class ProductService(val productClient: ProductClient,
                     val productDao: ProductDao,
                     val categoryService: CategoryService,
                     val productExpirationDao: ProductExpirationDao) {

    private val log = KotlinLogging.logger {}

    fun getHighlights(): Highlights? {
        val productApi = productClient.listAllProducts(true)
        if (productApi.isEmpty()) return null

        val categoryByExternalId = categoryService.getByIds(productApi.mapNotNull { it.categoryId })

        return Highlights(highlights = productApi.mapNotNull {
            val id = createRelation(it)
            val category = categoryByExternalId[it.categoryId]
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

        return Search(result = products.mapNotNull {
            val id = createRelation(it)
            val category = categoryByExternalId[it.categoryId]
            convertProduct(id, it, category)
        })
    }

    private fun convertProduct(id: Long, productApi: ProductApi, category: Category?): Product? {
        return if (productApi.groupId == "2d535b83-7b2a-48f7-9e3e-08b3a0b9bf12") {
            Product(id = id,
                    name = productApi.name.toLowerCase().capitalize(),
                    category = category?.name?.toLowerCase()?.capitalize() ?: "",
                    imageUrl = if (productApi.imageUrl.isNullOrEmpty()) null else productApi.imageUrl,
                    brand = productApi.brand?.toLowerCase()?.capitalize() ?: "Sem Marca",
                    description = productApi.description,
                    stock = productApi.stock.toLong(),
                    height = productApi.height,
                    weight = productApi.weight,
                    width = productApi.width,
                    length = productApi.length,
                    price = (productApi.price.multiply(BigDecimal.valueOf(100L))).toInt())
        } else {
            null
        }
    }

    private fun createRelation(product: ProductApi): Long {
        val externalId = product.id

        return productDao.findByExternalId(externalId) ?: productDao.insertExternalId(externalId)
               ?: throw IllegalStateException("Can't create relation between ids!")
    }

    fun reserveProducts(productsByQuantity: Map<Product, Long>): Boolean {
        // para cada produto, reservar a quantidade definida.
        // se der algum erro, deve desreservar os que deram certo anteriormente
        val successfulProducts: MutableMap<Product, Long> = emptyMap<Product, Long>().toMutableMap()

        for (entry in productsByQuantity) {
            val externalId = productDao.findById(entry.key.id)
            val result = productClient.reserve(UUID.fromString(externalId), entry.value)

            if (!result) {
                break
            }

            val quantity = productExpirationDao.getQuantity(entry.key.id)
            if (quantity == null) {
                productExpirationDao.addProduct(entry.key.id, entry.value)
            } else {
                productExpirationDao.updateQuantity(entry.key.id, quantity + entry.key.id)
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


    private fun releaseProductsId(productsByQuantity: Map<Long, Long>): Boolean{
        productsByQuantity.forEach { t, u ->
            val externalId = productDao.findById(t)
            val result = productClient.release(UUID.fromString(externalId), u)

            if (result) {
                val quantity = productExpirationDao.getQuantity(t)
                quantity?.let {
                    if (quantity > u) {
                        productExpirationDao.updateQuantity(t, quantity - u)
                    } else {
                        productExpirationDao.removeProduct(t, u)
                    }
                }
            }
        }

        return true
    }

    fun releaseProducts(productsByQuantity: Map<Product, Long>): Boolean {
        return releaseProductsId(productsByQuantity.map {
            it.key.id to it.value
        }.toMap())
    }

    fun releaseExpire(): Boolean {
        val listExpiredProducts = productExpirationDao.getExpired()
        if(listExpiredProducts.isEmpty()){
            return true
        }

        return releaseProductsId(listExpiredProducts.toMap())
    }

    fun removeExpire(products: Map<Product, Long>) {
        products.forEach { product, quantity ->
            val old = productExpirationDao.getQuantity(product.id)
            old?.let {
                if (old > quantity) {
                    productExpirationDao.updateQuantity(product.id, old - quantity)
                } else {
                    productExpirationDao.removeProduct(product.id, quantity)
                }
            }
        }
    }
}