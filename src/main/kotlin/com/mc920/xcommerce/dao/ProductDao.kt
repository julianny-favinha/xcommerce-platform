package com.mc920.xcommerce.dao

interface ProductDao {

    fun setRelation(id: Int, externalId: Int)

    fun getById(id: Int)

    fun getByExternalId(externalId: Int)

}