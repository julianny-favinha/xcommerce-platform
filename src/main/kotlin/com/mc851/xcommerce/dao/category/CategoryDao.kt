package com.mc851.xcommerce.dao.category

interface CategoryDao {

    fun findById(id: Long): String?

    fun insertExternalId(externalId: String): Long?

    fun findByExternalId(externalId: String): Long?

}