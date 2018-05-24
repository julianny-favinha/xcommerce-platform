package com.mc851.xcommerce.dao.token

import java.time.LocalDateTime
import java.util.UUID

interface TokenDao {

    fun createToken(token: UUID, userId: Long, expireTime: LocalDateTime): String?

    fun findTokenByUserId(userId: Long): String?

    fun findUserIdByToken(token: String): Long?

    fun checkToken(token: String): Boolean
}