package com.mc851.xcommerce.clients.clientes01

import com.mc851.xcommerce.clients.product01.api.ClientsApi
import com.mc851.xcommerce.clients.ClientesClient
import com.mc851.xcommerce.model.Cliente
import java.util.UUID

class ClientesClientOkHttp : ClientesClient {
    override fun signIn(id: UUID, email: String, password: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return true
    }

    override fun changePassword(id: UUID, password: String, samePass: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return true
    }

    override fun addAddress(id: UUID, cep: String, address: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return true
    }

    // TODO: Como são feitos os requests aqui?
    // Diferente dos já feitos, aqui são gets, não parsers
    // Possivelmente: https://github.com/square/okhttp/wiki/Recipes
    // em suma, montar um corpo e mandar
    override fun updateCliente(
            id: UUID,
            name: String?,
            email: String?,
            password: String?,
            samePass: String?,
            birthDate: String?,
            cpf: String?,
            gender: String?,
            telephone: String?
    ): String {

    }

    override fun signUp(
            name: String,
            email: String,
            password: String,
            samePass: String,
            birthDate: String?,
            cpf: String,
            gender: String?,
            telephone: String?
    ): String {

    }

    override fun getCliente(id: UUID): Cliente {

    }

}