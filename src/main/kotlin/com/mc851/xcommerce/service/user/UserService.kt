package com.mc851.xcommerce.service.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.UserClient
import com.mc851.xcommerce.clients.user01.api.RegisterAPI
import com.mc851.xcommerce.clients.user01.api.UpdateAPI
import com.mc851.xcommerce.clients.user01.api.UserAPI
import com.mc851.xcommerce.dao.user.UserDao
import com.mc851.xcommerce.model.api.*
import com.mc851.xcommerce.service.user.credential.UserCredentialService
import mu.KotlinLogging

class UserService(private val userClient: UserClient,
                  private val userDao: UserDao,
                  private val userCredentialService: UserCredentialService) {

    private val log = KotlinLogging.logger {}

    fun signUp(signUp: SignUp): SignInResponse? {
        val register = RegisterAPI(name = signUp.user.name,
                email = signUp.user.email,
                password = "private",
                samePass = "private",
                birthDate = signUp.user.birthDate,
                cpf = signUp.user.cpf,
                address = AddressConverter.toJson(signUp.user.address),
                gender = signUp.user.gender,
                telephone = signUp.user.telephone)

        log.info { "attempting to create user to $register" }

        val id = userClient.register(register) ?: return null
        val userInfo = userClient.getUserById(id) ?: throw IllegalStateException("Couldn't find User created! Bizarre!")

        log.info { "user created with externalID: $id"  }

        val userId = createUserRelation(id)
        userCredentialService.addCredential(signUp.user.email, signUp.user.password, userId)



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
                telephone = update.telephone,
                address = AddressConverter.toJson(update.address))
        userClient.update(externalId, updateInfo)
        val userInfo = userClient.getUserById(externalId) ?: throw IllegalStateException("Updated user not found!")

        return convertUser(userInfo, id)
    }

    fun findByUserId(id: Long): User? {
        log.info { "Attempt to find user $id"}
        val externalId = userDao.findById(id) ?: return null

        log.info { "For this external $externalId attempt to find info"}
        val userInfo = userClient.getUserById(externalId) ?: return null

        log.info { "Found $userInfo"}
        return convertUser(userInfo, id)
    }

    private fun convertUser(userAPI: UserAPI, userId: Long): User {
        return User(id = userId,
                name = userAPI.name,
                email = userAPI.email,
                password = userAPI.password,
                birthDate = userAPI.birthDate,
                cpf = userAPI.cpf,
                gender = userAPI.gender,
                telephone = userAPI.telephone,
                address = AddressConverter.fromJson(userAPI.address))
    }

    private fun createUserRelation(externalId: String): Long {
        return userDao.findByExternalId(externalId) ?: userDao.insertExternalId(externalId)
               ?: throw IllegalStateException("Can't insert user!")
    }

    object AddressConverter {

        internal fun toJson(address: AddressFull): String {
            return jacksonObjectMapper().writeValueAsString(address)
        }

        internal fun fromJson(rawAddress: String): AddressFull {
            return jacksonObjectMapper().readValue(rawAddress)
        }
    }
}
