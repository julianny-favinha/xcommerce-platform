package com.mc851.xcommerce.dao.credential

import com.mc851.xcommerce.model.UserCredential

interface UserCredentialDao {

    fun saveCredential(userCredential: UserCredential): Boolean?

    fun findCredentialByEmail(email: String): UserCredential?

}