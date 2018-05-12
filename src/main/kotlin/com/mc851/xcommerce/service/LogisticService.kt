package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.LogisticClient
import com.mc851.xcommerce.clients.product01.api.ProductApi
import com.mc851.xcommerce.dao.logistic.LogisticDao
import com.mc851.xcommerce.model.Shipment
import com.mc851.xcommerce.model.Product
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LogisticService(val logisticClient: LogisticClient,
                     val logisticDao: LogisticDao,
                     val logisticService: LogisticService) {

    fun getShipment(): Shipment? {
        val productApi = logisticClient.listAllProducts(true)
        if (productApi.isEmpty()) return null

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


    private fun createRelation(product: ProductApi): Long {
        val externalId = product.id

        return productDao.findByExternalId(externalId) ?: productDao.insertExternalId(externalId)
        ?: throw IllegalStateException("Can't create relation between ids!")
    }
}