package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.UserClient
import com.mc851.xcommerce.clients.user01.api.LoginAPI
import com.mc851.xcommerce.clients.user01.api.RegisterAPI
import com.mc851.xcommerce.clients.user01.api.UpdateAPI
import com.mc851.xcommerce.clients.user01.api.UserAPI
import com.mc851.xcommerce.dao.user.UserDao
import com.mc851.xcommerce.model.SignIn
import com.mc851.xcommerce.model.SignUp
import com.mc851.xcommerce.model.Update
import com.mc851.xcommerce.model.User
import org.springframework.stereotype.Service

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
        System.out.println(id)
        val userInfo = userClient.getUserById(id) ?: throw IllegalStateException("Couldn't find User created! Bizarre!")

        return convertUser(userInfo, id)
    }

    fun update(id:Long, update: Update): User {
        val externalId = userDao.findById(id) ?: throw IllegalStateException("User not found!")

        val updateInfo = UpdateAPI(name = update.name,
                password = update.password,
                samePass = update.password,
                birthDate = update.birthDate,
                gender = update.gender,
                telephone = update.telephone)

        userClient.update(externalId, updateInfo)
        val userInfo = userClient.getUserById(externalId) ?: throw IllegalStateException("Updated user not found!")

        return convertUser(userInfo, externalId)
    }

    // usa update
    fun changePass(id:String, pass: String): User {
        val externalId = userDao.findById(id.toLong()) ?: throw IllegalStateException("User not found!")

        val userInfo = userClient.getUserById(externalId) ?: throw IllegalStateException("User not found!")

        val updateInfo = UpdateAPI(name = userInfo.name,
                password = pass,
                samePass = pass,
                birthDate = userInfo.birthDate,
                gender = userInfo.gender,
                telephone = userInfo.telephone)

        val status = userClient.update(externalId, updateInfo)

        val newUserInfo = userClient.getUserById(externalId) ?: throw IllegalStateException("Updated user not found!")

        return convertUser(newUserInfo, id)
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
}