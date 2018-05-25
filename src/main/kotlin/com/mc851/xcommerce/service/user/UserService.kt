package com.mc851.xcommerce.service.user

import com.mc851.xcommerce.clients.UserClient
import com.mc851.xcommerce.clients.user01.api.RegisterAPI
import com.mc851.xcommerce.clients.user01.api.UpdateAPI
import com.mc851.xcommerce.clients.user01.api.UserAPI
import com.mc851.xcommerce.dao.user.UserDao
import com.mc851.xcommerce.model.SignIn
import com.mc851.xcommerce.model.SignInResponse
import com.mc851.xcommerce.model.SignUp
import com.mc851.xcommerce.model.Update
import com.mc851.xcommerce.model.User

class UserService(private val userClient: UserClient,
                  private val userDao: UserDao,
                  private val userCredentialService: UserCredentialService) {

    fun signUp(signUp: SignUp): SignInResponse? {
        val register = RegisterAPI(name = signUp.name,
            email = signUp.email,
            password = "private",
            samePass = "private",
            birthDate = signUp.birthDate,
            cpf = signUp.cpf,
            gender = signUp.gender,
            telephone = signUp.telephone)

        val id = userClient.register(register) ?: return null
        val userInfo = userClient.getUserById(id) ?: throw IllegalStateException("Couldn't find User created! Bizarre!")

        val userId = createUserRelation(id)
        userCredentialService.addCredential(signUp.email, signUp.password, userId)

        val user = convertUser(userInfo, userId)
        val token = userCredentialService.retrieveToken(userId)
        return SignInResponse(user, token)
    }

    fun signIn(signIn: SignIn): SignInResponse? {
        val userId = userCredentialService.verifyCredential(signIn.email, signIn.password) ?: return null
        val externalId = userDao.findById(userId) ?: throw IllegalStateException("Credential found but no relation")
        val userInfo = userClient.getUserById(externalId) ?: throw IllegalStateException("User not found!")

        val user = convertUser(userInfo, userId)
        val token = userCredentialService.retrieveToken(userId)
        return SignInResponse(user, token)
    }

    fun update(id: Long, update: Update): User {
        val externalId = userDao.findById(id) ?: throw IllegalStateException("User not found!")

        val updateInfo = UpdateAPI(name = update.name,
            password = update.password,
            samePass = update.password,
            birthDate = update.birthDate,
            gender = update.gender,
            telephone = update.telephone)

        userClient.update(externalId, updateInfo)
        val userInfo = userClient.getUserById(externalId) ?: throw IllegalStateException("Updated user not found!")

        return convertUser(userInfo, id)
    }

    fun findByUserId(id: Long): User? {
        val externalId = userDao.findById(id) ?: throw IllegalStateException("User not found!")

        val userInfo = userClient.getUserById(externalId) ?: throw IllegalStateException("User not found!")

        return convertUser(userInfo, id)
    }

    private fun convertUser(userAPI: UserAPI, userId: Long): User {
        return User(id = userId,
            name = userAPI.name,
            email = userAPI.email,
            birthDate = userAPI.birthDate,
            cpf = userAPI.cpf,
            gender = userAPI.gender,
            telephone = userAPI.telephone)
    }

    private fun createUserRelation(externalId: String): Long {
        return userDao.findByExternalId(externalId) ?: userDao.insertExternalId(externalId)
               ?: throw IllegalStateException("Can't insert user!")
    }
}
