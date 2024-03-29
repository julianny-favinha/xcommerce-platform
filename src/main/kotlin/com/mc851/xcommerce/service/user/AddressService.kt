package com.mc851.xcommerce.service.user

import com.mc851.xcommerce.clients.AddressClient
import com.mc851.xcommerce.clients.address.api.AddressApi
import com.mc851.xcommerce.model.api.Address

class AddressService(val addressClient: AddressClient) {

    fun checkCep(cep: String): Address? {
        val addressApi = addressClient.findByCep(cep) ?: return null
        return convertAddress(addressApi)
    }

    private fun convertAddress(addressApi: AddressApi): Address {
        return Address(cep = addressApi.cep,
                logradouro = addressApi.logradouro,
                neighborhood = addressApi.bairro,
                city = addressApi.cidade,
                state = addressApi.uf)
    }
}