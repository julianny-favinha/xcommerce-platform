package com.mc920.xcommerce.dao

import org.springframework.jdbc.core.JdbcTemplate

class ProductDaoPostgres(val jdbcTemplate: JdbcTemplate) : ProductDao {

    override fun findById(id: Long): Long? {
        return jdbcTemplate.query("SELECT external_id FROM product_relation", { rs, _ ->
            rs.getLong("external_id")
        }).firstOrNull()

    }

    override fun insertExternalId(externalId: Long): Long? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByExternalId(externalId: Long): Long? {
        return jdbcTemplate.query("SELECT id FROM product_relation", { rs, _ ->
            rs.getLong("id")
        }).firstOrNull()
    }
}