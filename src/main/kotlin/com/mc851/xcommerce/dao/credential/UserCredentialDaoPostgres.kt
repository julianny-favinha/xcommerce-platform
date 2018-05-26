package com.mc851.xcommerce.dao.credential

import com.mc851.xcommerce.model.internal.UserCredential
import org.springframework.jdbc.core.JdbcTemplate

class Queries {
    companion object {
        const val INSERT_CREDENTIAL =
            """INSERT INTO xcommerce.user_credential(password, email, user_id) VALUES (?, ?, ?) RETURNING id"""
        const val FIND_BY_EMAIL = """SELECT user_id, email, password FROM xcommerce.user_credential WHERE email = ?"""
    }
}

class UserCredentialDaoPostgres(private val jdbcTemplate: JdbcTemplate) : UserCredentialDao {

    override fun saveCredential(userCredential: UserCredential): Boolean? {
        return jdbcTemplate.query(Queries.INSERT_CREDENTIAL, { ps ->
            ps.setString(1, userCredential.password)
            ps.setString(2, userCredential.email)
            ps.setLong(3, userCredential.userId)
        }, { rs, _ ->
            rs.getLong("id")
        }).firstOrNull()?.let { true }
    }

    override fun findCredentialByEmail(email: String): UserCredential? {
        return jdbcTemplate.query(Queries.FIND_BY_EMAIL, { ps ->
            ps.setString(1, email)
        }, { rs, _ ->
            UserCredential(rs.getLong("user_id"), rs.getString("email"), rs.getString("password"))
        }).firstOrNull()
    }

}