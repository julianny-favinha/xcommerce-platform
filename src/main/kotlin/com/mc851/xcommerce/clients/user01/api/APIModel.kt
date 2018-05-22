package com.mc851.xcommerce.clients.user01.api

data class UserAPI(val name: String,
                   val email: String,
                   val password: String,
                   val cpf: String,
                   val birthDate: String?,
                   val cep: String?,
                   val address: String?,
                   val gender: String?,
                   val telephone: String?)

data class RegisterAPI(val name: String,
                       val email: String,
                       val password: String,
                       val samePass: String,
                       val birthDate: String?,
                       val cpf: String,
                       val gender: String?,
                       val telephone: String?)

data class UpdateAPI(val name: String,
                       val password: String,
                       val samePass: String,
                       val birthDate: String?,
                       val gender: String?,
                       val telephone: String?)

data class LoginAPI(val email: String,
                       val password: String)