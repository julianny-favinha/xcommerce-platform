package com.mc851.xcommerce.controller

import com.mc851.xcommerce.filters.RequestContext
import com.mc851.xcommerce.model.SignIn
import com.mc851.xcommerce.model.SignInResponse
import com.mc851.xcommerce.model.SignUp
import com.mc851.xcommerce.model.Update
import com.mc851.xcommerce.model.User
import com.mc851.xcommerce.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PutMapping("/update")
    fun update(@ModelAttribute(RequestContext.CONTEXT) context: RequestContext, @RequestBody update: Update): ResponseEntity<User> {
        val response = userService.update(context.userId, update)
        return handleResponse(response)
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody signUp: SignUp): ResponseEntity<SignInResponse> {
        val response = userService.signUp(signUp)
        return handleErrorResponse(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody signIn: SignIn): ResponseEntity<SignInResponse> {
        val response = userService.signIn(signIn)
        return handleErrorResponse(response)
    }

    @GetMapping("")
    fun getUser(@ModelAttribute(RequestContext.CONTEXT) context: RequestContext): ResponseEntity<User> {
        val response = userService.findByUserId(context.userId)
        return handleErrorResponse(response)
    }

}