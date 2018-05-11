package com.mc851.xcommerce.model

data class Cliente(
        val id: Int,
        val name: String,
        val email: String,
        val password: String,
        val birthDate: String?,
        val cpf: String,
        val gender: String?,
        val telephone: String?
)
