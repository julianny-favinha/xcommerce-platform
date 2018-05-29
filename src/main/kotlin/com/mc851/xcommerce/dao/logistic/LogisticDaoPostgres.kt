package com.mc851.xcommerce.dao.logistic

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.dao.product.Queries
import org.springframework.jdbc.core.JdbcTemplate

class Queries {
    companion object {
        const val INSERT_EXTERNAL_ID =
            """INSERT INTO xcommerce.logistic_relation(external_id) VALUES (?::jsonb) RETURNING id"""
        const val FIND_BY_ID = """SELECT external_id FROM xcommerce.logistic_relation WHERE id = ?"""
    }
}

class LogisticDaoPostgres(private val jdbcTemplate: JdbcTemplate) : LogisticDao {
    override fun findById(id: Long): List<String> {
        return ExternalIdsConverter.fromJson(jdbcTemplate.query(Queries.FIND_BY_ID, { ps ->
            ps.setLong(1, id)
        }, { rs, _ ->
            rs.getString("external_id")
        }).firstOrNull())

    }

    override fun insertExternalIds(externalId: List<String>): Long? {
        return jdbcTemplate.query(Queries.INSERT_EXTERNAL_ID, { ps ->
            ps.setString(1, ExternalIdsConverter.toJson(externalId))
        }, { rs, _ ->
            rs.getLong("id")
        }).firstOrNull()
    }

}

object ExternalIdsConverter {

    internal fun toJson(externalIds: List<String>): String {
        return jacksonObjectMapper().writeValueAsString(externalIds)
    }

    internal fun fromJson(rawExternalIds: String?): List<String> {
        if (rawExternalIds == null) {
            return emptyList()
        }
        return jacksonObjectMapper().readValue(rawExternalIds)
    }
}