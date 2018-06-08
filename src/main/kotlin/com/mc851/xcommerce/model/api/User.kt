package com.mc851.xcommerce.model.api

data class User(val id: Long,
                val name: String,
                val email: String,
                val password: String,
                val birthDate: String?,
                val cpf: String,
                val address: AddressFull,
                val gender: String?,
                val telephone: String?)

data class SignUp(val user: User)

data class Update(val name: String,
                  val password: String,
                  val birthDate: String?,
                  val gender: String?,
                  val telephone: String?,
                  val address: AddressFull)

data class SignIn(val email: String, val password: String)

data class SignInResponse(val user: User, val token: String)
