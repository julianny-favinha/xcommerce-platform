package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.User
import com.mc851.xcommerce.service.CreditService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("credit")
class CreditController {
    @Autowired
    lateinit var creditService: CreditService

    @GetMapping("/score")
    fun validate(): ResponseEntity<Boolean> {
        val user = User(1, "Igor", "igor", null, "26850989928", null, null)
        val response = creditService.validateUser(user, 1000L)

        return handleResponse(response)
    }
}