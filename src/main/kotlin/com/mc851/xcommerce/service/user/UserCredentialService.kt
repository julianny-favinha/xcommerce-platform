package com.mc851.xcommerce.service.user

import com.mc851.xcommerce.dao.credential.UserCredentialDao
import com.mc851.xcommerce.dao.token.TokenDao
import com.mc851.xcommerce.model.UserCredential
import com.mc851.xcommerce.service.user.PasswordUtils.Companion.checkpw
import com.mc851.xcommerce.service.user.PasswordUtils.Companion.hashpw
import org.apache.commons.validator.routines.EmailValidator
import java.time.LocalDateTime
import java.util.UUID

class UserCredentialService(private val userCredentialDao: UserCredentialDao, private val tokenDao: TokenDao) {
    companion object {
        private val EXPIRE_TIME = LocalDateTime.now().plusDays(3)
    }

    fun addCredential(email: String, password: String, userId: Long): Boolean {
        if (!EmailValidator.getInstance().isValid(email)) return false

        return userCredentialDao.saveCredential(UserCredential(userId = userId,
            email = email.toUpperCase(),
            password = hashpw(password)))
               ?: throw IllegalStateException("Could not create user credential, email duplication should already be taken care of")
    }

    fun verifyCredential(email: String, password: String): Long? {
        if (!EmailValidator.getInstance().isValid(email)) return null

        val userCredential = userCredentialDao.findCredentialByEmail(email.toUpperCase()) ?: return null
        return if (checkpw(password, userCredential.password)) userCredential.userId else null
    }

    fun retrieveUser(token: String): Long {
        return tokenDao.findUserIdByToken(token) ?: throw IllegalStateException("Couldn't found user for token")
    }

    fun retrieveToken(userId: Long): String {
        return tokenDao.findTokenByUserId(userId) ?: tokenDao.createToken(UUID.randomUUID(), userId, EXPIRE_TIME)
               ?: throw IllegalStateException("Couldn't create token for user!")
    }

    fun verifyToken(token: String): Boolean {
        return tokenDao.checkToken(token)
    }

}