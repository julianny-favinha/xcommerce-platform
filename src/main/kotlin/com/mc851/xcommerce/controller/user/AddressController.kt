package com.mc851.xcommerce.controller.user

import com.mc851.xcommerce.controller.utils.handleResponse
import com.mc851.xcommerce.model.api.Address
import com.mc851.xcommerce.service.user.AddressService
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

    @GetMapping("check/{cep}")
    fun checkCep(@PathVariable(name = "cep", required = true) cep: String): ResponseEntity<Address> {
        val address = addressService.checkCep(cep)
        return handleResponse(address)
    }
}