package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.SignIn
import com.mc851.xcommerce.model.SignUp
import com.mc851.xcommerce.model.User
import com.mc851.xcommerce.model.Update
import com.mc851.xcommerce.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PutMapping("/update/{id}")
    fun update(@PathVariable id: Long, @RequestBody update: Update): ResponseEntity<User> {
        val response = userService.update(id, update)
        return handleResponse(response)
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody signUp: SignUp): ResponseEntity<User> {
        val response = userService.signUp(signUp)
        return handleErrorResponse(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody signIn: SignIn): ResponseEntity<User> {
        val response = userService.signIn(signIn)
        return handleErrorResponse(response)
    }

//    @GetMapping("/test")
//    fun test(httpSession: HttpSession): ResponseEntity<Boolean> {
//        System.out.println(httpSession.servletContext.toString())
//        val attribute = httpSession.getAttribute("a")
//        if(attribute == "abc"){
//            return handleResponse(true)
//        }
//        return handleResponse(false)
//    }

}