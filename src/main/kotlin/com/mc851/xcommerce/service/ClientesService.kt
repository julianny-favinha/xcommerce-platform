package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.ClientesClient
import com.mc851.xcommerce.clients.clientes01.api.ClientesApi
import com.mc851.xcommerce.dao.clientes.ClientesDao
import com.mc851.xcommerce.model.Cliente
import org.springframework.stereotype.Service

@Service
class ClientesService(
        val clientesClient: ClientesClient,
        val clientesDao: ClientesDao) {

    fun getById(id: Long): Cliente? {
        val externalId = clientesDao.findById(id) ?: return null
        val cliente = clientesClient.getCliente(UUID.fromString(externalId)) ?: return null

        return convertCliente(id, cliente)
    }

    private fun convertCliente(id: Long, clientesApi: ClientesApi): Cliente {
        return Cliente(id = id.toInt(),
                name = clientesApi.name ?: throw IllegalStateException("Cliente doesn't have name"),
                email = clientesApi.email ?: throw IllegalStateException("Cliente doesn't have email"),
                password = clientesApi.password ?: throw IllegalStateException("Cliente doesn't have password"),
                birthDate = clientesApi.birthDate ?: "",
                cpf = clientesApi.cpf ?: throw IllegalStateException("Cliente doesn't have cpf"),
                gender = clientesApi.telephone ?: "",
                telephone = clientesApi.telephone ?: ""
        )
    }


}