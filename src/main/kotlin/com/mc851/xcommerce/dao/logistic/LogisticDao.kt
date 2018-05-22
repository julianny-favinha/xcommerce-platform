package com.mc851.xcommerce.dao.logistic

interface LogisticDao {
    fun findById(id: Long): String?

    fun insertExternalId(externalId: String): Long?

    fun findByExternalId(externalId: String): Long?
}