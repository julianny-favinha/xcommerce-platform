package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.UserClient
import com.mc851.xcommerce.clients.user01.api.RegisterAPI
import com.mc851.xcommerce.clients.user01.api.UserAPI
import com.mc851.xcommerce.dao.user.UserDao
import com.mc851.xcommerce.model.SignUp
import com.mc851.xcommerce.model.User
import org.springframework.stereotype.Service

@Service
class UserService(val userClient: UserClient, val userDao: UserDao) {

    fun signUp(signUp: SignUp): User? {
        val register = RegisterAPI(name = signUp.name,
            email = signUp.email,
            password = signUp.password,
            samePass = signUp.password,
            birthDate = signUp.birthDate,
            cpf = signUp.cpf,
            gender = signUp.gender,
            telephone = signUp.telephone)

        val id = userClient.register(register) ?: return null
        val userInfo = userClient.getUserById(id) ?: throw IllegalStateException("Couldn't find User created! Bizarre!")

        // SignIn

        return convertUser(userInfo, id)
    }

    private fun convertUser(userAPI: UserAPI, externalId: String): User {
        val id =
            userDao.findByExternalId(externalId) ?: userDao.insertExternalId(externalId) ?: throw IllegalStateException(
                "Can't insert user!")
        return User(id = id.toInt(),
            name = userAPI.name,
            email = userAPI.email,
            birthDate = userAPI.birthDate,
            cpf = userAPI.cpf,
            gender = userAPI.gender,
            telephone = userAPI.telephone)
    }

    //    fun getById(id: Long): User? {
    //        val externalId = clientesDao.findById(id) ?: return null
    //        val cliente = clientesClient.getCliente(UUID.fromString(externalId)) ?: return null
    //
    //        return convertCliente(id, cliente)
    //    }
    //
    //    private fun convertCliente(id: Long, clientesApi: ClientesApi): User {
    //        return User(id = id.toInt(),
    //                name = clientesApi.name ?: throw IllegalStateException("User doesn't have name"),
    //                email = clientesApi.email ?: throw IllegalStateException("User doesn't have email"),
    //                password = clientesApi.password ?: throw IllegalStateException("User doesn't have password"),
    //                birthDate = clientesApi.birthDate ?: "",
    //                cpf = clientesApi.cpf ?: throw IllegalStateException("User doesn't have cpf"),
    //                gender = clientesApi.telephone ?: "",
    //                telephone = clientesApi.telephone ?: ""
    //        )
    //    }

}