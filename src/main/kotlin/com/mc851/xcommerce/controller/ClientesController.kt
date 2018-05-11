package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.Cliente
import com.mc851.xcommerce.service.ClientesService
import org.springframework.http.ResponseEntity

class ClientesController {
    lateinit var clienteService: ClientesService

    // TODO: funções a serem usadas pela front
    // Dúvida: Front chama funções a partir desse módulo?

    // getCliente
    fun getClienteById(id: Long): ResponseEntity<Cliente>{
        val cliente = clienteService.getById(id)
        return handleResponse(cliente)
    }

    // signUp
    // updateCliente
    // addAddress
    // changePassword
    // signIn

}