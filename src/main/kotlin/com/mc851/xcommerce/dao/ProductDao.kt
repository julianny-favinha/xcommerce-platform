package com.mc851.xcommerce.dao

interface ProductDao {

    fun findById(id: Long): String?

    fun insertExternalId(externalId: String): Long?

    fun findByExternalId(externalId: String): Long?

}