package com.mc851.xcommerce.clients.address

import com.mc851.xcommerce.clients.address.api.*
import com.mc851.xcommerce.model.Address
import com.mc851.xcommerce.model.Cep

interface AddressClient {

    fun findByCep(cep: String): AddressApi?

    fun insertCep(cep: String, logradouro: String, bairro: String, idCidade: String): CepApi?

    fun listCities(uf: String, query: String): List<CityApi>

    fun insertCity(uf: String, nome:String): IdApi?

    fun listStates(): List<StateApi>

    fun insertState(uf: String, nome: String): IdApi?
}