package com.mc851.xcommerce.dao.logistic

interface LogisticDao {
    fun findById(id: Long): List<String>

    fun insertExternalIds(externalId: List<String>): Long?

}