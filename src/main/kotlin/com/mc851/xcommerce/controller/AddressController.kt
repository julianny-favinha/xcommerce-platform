package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.Address
import com.mc851.xcommerce.service.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("address")
class AddressController {

    @Autowired
    lateinit var addressService: AddressService

    @GetMapping("/checkCep")
    fun checkCep(@PathVariable(name = "cep", required = true) cep: String): ResponseEntity<Address> {
        val address = addressService.checkCep(cep)
        return handleResponse(address)
    }
}