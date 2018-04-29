package com.mc920.xcommerce.dao

import org.springframework.jdbc.core.JdbcTemplate
import java.sql.PreparedStatement

class Queries {
    companion object {
        const val INSERT_EXTERNAL_ID = """INSERT INTO xcommerce.product_relation(external_id) VALUES (?) RETURNING id"""
        const val FIND_BY_ID = """SELECT external_id FROM xcommerce.product_relation"""
        const val FIND_BY_EXTERNAL_ID = """SELECT id FROM xcommerce.product_relation"""
    }
}

class ProductDaoPostgres(private val jdbcTemplate: JdbcTemplate) : ProductDao {

    override fun findById(id: Long): Long? {
        return jdbcTemplate.query(Queries.FIND_BY_ID, { rs, _ ->
            rs.getLong("external_id")
        }).firstOrNull()

    }

    override fun insertExternalId(externalId: Long): Long? {
        return jdbcTemplate.query(Queries.INSERT_EXTERNAL_ID,
            { ps: PreparedStatement ->
                ps.setLong(1, externalId) },
            { rs, _ ->
                rs.getLong("id") }).firstOrNull()
    }

    override fun findByExternalId(externalId: Long): Long? {
        return jdbcTemplate.query(Queries.FIND_BY_EXTERNAL_ID, { rs, _ ->
            rs.getLong("id")
        }).firstOrNull()
    }
}