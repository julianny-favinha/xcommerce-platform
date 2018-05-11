package com.mc851.xcommerce.clients.clientes01.api

data class ClientesApi(
        val id: String,
        val name: String?,
        val email: String?,
        val password: String?,
        val birthDate: String?,
        val cpf: String?,
        val gender: String?,
        val telephone: String?,
        val createdAt: Long?,
        val updatedAt: Long?
)