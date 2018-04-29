package com.mc920.xcommerce.dao

interface ProductDao {

    fun findById(id: Long): Long

    fun insertExternalId(externalId: Long): Long

    fun findByExternalId(externalId: Long): Long

}