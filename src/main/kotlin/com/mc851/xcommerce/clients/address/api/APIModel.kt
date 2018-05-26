package com.mc851.xcommerce.clients.address.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AddressApi(val cep: String, val logradouro: String, val bairro: String, val cidade: String, val uf: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class CepApi(val cep: String)

data class CityApi(val id: Int, val nome: String)

data class IdApi(val id: Int)

data class StateApi(val id: Int, val uf: String, val nome: String)