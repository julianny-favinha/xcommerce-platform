package com.mc851.xcommerce.dao.clientes

import org.springframework.jdbc.core.JdbcTemplate

class Queries {
    companion object {
        const val INSERT_EXTERNAL_ID = """INSERT INTO xcommerce.product_relation(external_id) VALUES (?) RETURNING id"""
        const val FIND_BY_ID = """SELECT external_id FROM xcommerce.product_relation WHERE id = ?"""
        const val FIND_BY_EXTERNAL_ID = """SELECT id FROM xcommerce.product_relation WHERE external_id = ?"""
    }
}

class ClientesDaoPostgres(private val jdbcTemplate: JdbcTemplate) : ClientesDao {

    override fun findById(id: Long): String? {
        return jdbcTemplate.query(Queries.FIND_BY_ID, { ps ->
            ps.setLong(1, id)
        }, { rs, _ ->
            rs.getString("external_id")
        }).firstOrNull()

    }

    override fun insertExternalId(externalId: String): Long? {
        return jdbcTemplate.query(Queries.INSERT_EXTERNAL_ID, { ps ->
            ps.setString(1, externalId)
        }, { rs, _ ->
            rs.getLong("id")
        }).firstOrNull()
    }

    override fun findByExternalId(externalId: String): Long? {
        return jdbcTemplate.query(Queries.FIND_BY_EXTERNAL_ID, { ps ->
            ps.setString(1, externalId)
        }, { rs, _ ->
            rs.getLong("id")
        }).firstOrNull()
    }
}