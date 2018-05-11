package com.mc851.xcommerce.dao.clientes

interface ClientesDao {

    fun findById(id: Long): String?

    fun insertExternalId(externalId: String): Long?

    fun findByExternalId(externalId: String): Long?

}