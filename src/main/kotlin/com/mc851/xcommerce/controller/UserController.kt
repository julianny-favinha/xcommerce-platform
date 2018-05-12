package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.SignUp
import com.mc851.xcommerce.model.User
import com.mc851.xcommerce.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/update")
    fun update(@RequestBody user: User) {
        TODO()
    }

    @PostMapping("/signUp")
    fun signUp(@RequestBody signUp: SignUp): ResponseEntity<User> {
        val response = userService.signUp(signUp)
        return handleResponse(response)
    }

    // addAddress
    // changePassword
    // signIn

}