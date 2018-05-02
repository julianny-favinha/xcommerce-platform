package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.ProductClient
import com.mc851.xcommerce.dao.ProductDao
import com.mc851.xcommerce.model.Categories
import com.mc851.xcommerce.model.Category
import org.springframework.stereotype.Service

@Service
class CategoryService(val productClient: ProductClient, val productDao: ProductDao) {

    fun getAll(): Categories {
        return TODO()
    }

    fun getByIds(externalIds: List<String>): Map<String, Category> {
        return TODO()
    }

    fun getById(externalId: String): Category {
        return TODO()
    }
}