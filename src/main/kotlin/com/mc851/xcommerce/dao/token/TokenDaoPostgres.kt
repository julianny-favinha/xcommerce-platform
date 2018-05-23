package com.mc851.xcommerce.dao.token

import org.springframework.jdbc.core.JdbcTemplate
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.UUID

class Queries {
    companion object {
        const val INSERT_TOKEN =
            """INSERT INTO xcommerce.user_token(user_id, expire_at, token) VALUES (?, ?, ?) RETURNING token"""
        const val FIND_TOKEN_BY_USER = """SELECT token FROM xcommerce.user_token WHERE user_id = ?"""
        const val CHECK_TOKEN = """SELECT token FROM xcommerce.user_token WHERE token = ? AND expire_at > ?"""
    }
}

class TokenDaoPostgres(val jdbcTemplate: JdbcTemplate) : TokenDao {
    override fun createToken(token: UUID, userId: Long, expireTime: LocalDateTime): String? {
        return jdbcTemplate.query(Queries.INSERT_TOKEN, { ps ->
            ps.setLong(1, userId)
            ps.setTimestamp(2, Timestamp.valueOf(expireTime))
            ps.setString(3, token.toString())
        }, { rs, _ ->
            rs.getString("token")
        }).firstOrNull()
    }

    override fun findTokenByUserId(userId: Long): String? {
        return jdbcTemplate.query(Queries.FIND_TOKEN_BY_USER, { ps ->
            ps.setLong(1, userId)
        }, { rs, _ ->
            rs.getString("token")
        }).firstOrNull()
    }

    override fun checkToken(token: String): Boolean {
        return jdbcTemplate.query(Queries.CHECK_TOKEN, { ps ->
            ps.setString(1, token)
            ps.setTimestamp(2, Timestamp.valueOf(now()))
        }, { rs, _ ->
            rs.getString("token")
        }).isNotEmpty()
    }

}