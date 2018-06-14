package com.mc851.xcommerce.dao.product

import org.springframework.jdbc.core.JdbcTemplate
import java.sql.PreparedStatement

class ProductDaoPostgres(private val jdbcTemplate: JdbcTemplate) : ProductDao {

    companion object {
        const val INSERT_EXTERNAL_ID = """INSERT INTO xcommerce.product_relation(external_id) VALUES (?) RETURNING id"""
        const val FIND_BY_ID = """SELECT external_id FROM xcommerce.product_relation WHERE id = ?"""
        const val FIND_BY_EXTERNAL_ID = """SELECT id FROM xcommerce.product_relation WHERE external_id = ?"""
    }

    override fun findById(id: Long): String? {
        return jdbcTemplate.query(FIND_BY_ID, { ps ->
            ps.setLong(1, id)
        }, { rs, _ ->
            rs.getString("external_id")
        }).firstOrNull()

    }

    override fun insertExternalId(externalId: String): Long? {
        return jdbcTemplate.query(INSERT_EXTERNAL_ID, { ps ->
            ps.setString(1, externalId)
        }, { rs, _ ->
            rs.getLong("id")
        }).firstOrNull()
    }

    override fun findByExternalId(externalId: String): Long? {
        return jdbcTemplate.query(FIND_BY_EXTERNAL_ID, { ps ->
            ps.setString(1, externalId)
        }, { rs, _ ->
            rs.getLong("id")
        }).firstOrNull()
    }
}

class ProductExpirationDaoPostgres(private val jdbcTemplate: JdbcTemplate) : ProductExpirationDao {
    companion object {
        const val INSERT_PRODUCT = """INSERT INTO xcommerce.product_expiration(product_id, quantity, created_at, valid_until) VALUES (?, ?, now(), now() + INTERVAL '30 minutes') RETURNING product_id"""
        const val GET_QUANTITY = """SELECT quantity FROM xcommerce.product_expiration WHERE product_id = ?"""
        const val UPDATE_QUANTITY = """UPDATE xcommerce.product_expiration SET quantity = ? WHERE product_id = ?"""
        const val REMOVE_PRODUCT = """DELETE FROM xcommerce.product_expiration WHERE product_id = ?"""
        const val FIND_EXPIRED = """SELECT product, quantity FROM xcommerce.product_expiration WHERE valid_until <= now()"""
    }

    override fun getExpired(): List<Pair<Long, Long>> {
        return jdbcTemplate.query(FIND_EXPIRED, { rs, _ -> rs.getLong("product_id") to rs.getLong("quantity") })
    }

    override fun getQuantity(id: Long): Long? {
        return jdbcTemplate.query(GET_QUANTITY, { ps -> ps.setLong(1, id) }, { rs, _ -> rs.getLong("quantity") }).firstOrNull()
    }

    override fun addProduct(id: Long, quantity: Long): Boolean {
        return jdbcTemplate.query(INSERT_PRODUCT,
                { ps ->
                    ps.setLong(1, id)
                    ps.setLong(2, quantity)
                },
                { rs, _ ->
                    rs.getLong("product_id")
                }).isNotEmpty()
    }

    override fun removeProduct(id: Long, quantity: Long): Boolean {
        return jdbcTemplate.update(REMOVE_PRODUCT,
                { ps: PreparedStatement ->
                    ps.setLong(1, id)
                }) > 0
    }

    override fun updateQuantity(id: Long, quantiy: Long): Boolean {
        return jdbcTemplate.update(UPDATE_QUANTITY, { ps ->
            ps.setLong(1, quantiy)
            ps.setLong(2, id)
        }) > 0
    }


}