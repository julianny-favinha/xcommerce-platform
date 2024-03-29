package com.mc851.xcommerce.clients

import com.mc851.xcommerce.clients.user01.api.RegisterAPI
import com.mc851.xcommerce.clients.user01.api.UpdateAPI
import com.mc851.xcommerce.clients.user01.api.UserAPI

interface UserClient {

    fun register(registerAPI: RegisterAPI): String?

    fun getUserById(id: String): UserAPI?

    fun update(id: String, updateAPI: UpdateAPI)

}