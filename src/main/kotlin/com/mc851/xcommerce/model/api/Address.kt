package com.mc851.xcommerce.model.api

data class Address(val cep: String, val logradouro: String, val neighborhood: String, val city: String, val state: String)

data class AddressFull(val address: Address, val number: Int, val complement: String?)