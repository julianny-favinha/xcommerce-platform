package com.mc851.xcommerce.model

data class User(val id: Int,
                val name: String,
                val email: String,
                val birthDate: String?,
                val cpf: String,
                val gender: String?,
                val telephone: String?)

data class SignUp(val name: String,
                  val email: String,
                  val password: String,
                  val birthDate: String?,
                  val cpf: String,
                  val gender: String?,
                  val telephone: String?)

data class SignIn(val email: String, val password: String)