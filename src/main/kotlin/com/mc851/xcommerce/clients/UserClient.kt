package com.mc851.xcommerce.clients

import com.mc851.xcommerce.clients.user01.api.RegisterAPI
import com.mc851.xcommerce.clients.user01.api.UserAPI

interface UserClient {

    fun register(registerAPI: RegisterAPI): String?

    fun getUserById(id: String): UserAPI?

//    fun getCliente(id: UUID): ClientesApi
//
//    // TODO: por enquanto ignorando parâmetros de endereço no signUp.
//    fun signUp(
//
//    ): String
//
//    // TODO: por enquanto ignorando parâmetros de endereço no update.
//    // TODO: pergunta: tudo pode ser opcional?
//    fun updateCliente(
//            id: UUID,
//            name: String?,
//            email: String?,
//            password: String?,
//            samePass: String?,
//            birthDate: String?,
//            cpf: String?,
//            gender: String?,
//            telephone: String?
//    ): String
//
//    // TODO: os métodos abaixo retornam True, por enquanto.
//
//    fun addAddress(id: UUID, cep: String, address: String): Boolean
//
//    fun changePassword(id: UUID, password: String, samePass: String): Boolean
//
//    fun signIn(id: UUID, email: String, password: String): Boolean
}