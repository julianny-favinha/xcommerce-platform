package com.mc851.xcommerce.dao.product

interface ProductDao {

    fun findById(id: Long): String?

    fun insertExternalId(externalId: String): Long?

    fun findByExternalId(externalId: String): Long?

}

interface ProductExpirationDao {

    fun getExpired(): List<Pair<Long, Long>>

    fun getQuantity(id: Long): Long?

    fun addProduct(id: Long, quantity: Long): Boolean

    fun removeProduct(id: Long, quantity: Long): Boolean
    fun updateQuantity(id: Long, quantiy: Long): Boolean
}