package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.ProductClient
import com.mc851.xcommerce.clients.product01.api.CategoryApi
import com.mc851.xcommerce.dao.category.CategoryDao
import com.mc851.xcommerce.model.Categories
import com.mc851.xcommerce.model.Category
import java.util.UUID

class CategoryService(private val productClient: ProductClient, private val categoryDao: CategoryDao) {

    fun getAll(): Categories {
        return Categories(productClient.listAllCategories().map { convertCategory(it) })
    }

    fun getByIds(externalIds: List<String>): Map<String, Category> {
        return externalIds.mapNotNull { id ->
            getById(id)?.let { id to it }
        }.toMap()
    }

    fun getById(externalId: String): Category? {
        return productClient.findCategoryById(UUID.fromString(externalId))?.let { convertCategory(it) }
    }

    private fun convertCategory(categoryApi: CategoryApi): Category {
        val id = categoryDao.findByExternalId(categoryApi.id) ?: categoryDao.insertExternalId(categoryApi.id)
                 ?: throw IllegalStateException("Couldn't insert in database")
        return Category(id = id, name = categoryApi.name)
    }
}