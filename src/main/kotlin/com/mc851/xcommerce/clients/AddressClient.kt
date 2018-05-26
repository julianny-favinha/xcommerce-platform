package com.mc851.xcommerce.clients.address

import com.mc851.xcommerce.clients.address.api.AddressApi
import com.mc851.xcommerce.clients.address.api.CepApi
import com.mc851.xcommerce.clients.address.api.CityApi
import com.mc851.xcommerce.clients.address.api.IdApi
import com.mc851.xcommerce.clients.address.api.StateApi

interface AddressClient {

    fun findByCep(cep: String): AddressApi?

    fun insertCep(cep: String, logradouro: String, bairro: String, idCidade: String): CepApi?

    fun listCities(uf: String, query: String): List<CityApi>

    fun insertCity(uf: String, nome: String): IdApi?

    fun listStates(): List<StateApi>

    fun insertState(uf: String, nome: String): IdApi?
}