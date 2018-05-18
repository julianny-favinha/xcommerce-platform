package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.SignIn
import com.mc851.xcommerce.model.SignUp
import com.mc851.xcommerce.model.User
import com.mc851.xcommerce.model.Update
import com.mc851.xcommerce.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PutMapping("/update/{id}")
    fun update(@PathVariable id: String, @RequestBody update: Update): ResponseEntity<User> {
        val response = userService.update(id, update)
        return handleResponse(response)
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody signUp: SignUp): ResponseEntity<User> {
        val response = userService.signUp(signUp)
        return handleResponse(response)
    }

    @PutMapping("/changePass/{id}")
    fun changePass(@PathVariable id: String, @RequestBody password: String): ResponseEntity<User> {
        val response = userService.changePass(id, password)
        return handleResponse(response)
    }

    // addAddress

    @GetMapping("/login")
    fun signIn(@RequestBody login: SignIn): ResponseEntity<User> {
        val response = userService.signIn(login)
        return handleResponse(response)
    }
}