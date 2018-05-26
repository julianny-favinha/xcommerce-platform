package com.mc851.xcommerce.dao.credential

import com.mc851.xcommerce.model.internal.UserCredential

interface UserCredentialDao {

    fun saveCredential(userCredential: UserCredential): Boolean?

    fun findCredentialByEmail(email: String): UserCredential?

}