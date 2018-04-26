package com.mc920.xcommerce.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("cart")
class CartController {

    @GetMapping("/checkout")
    fun checkout(): List<String> {
        return listOf("A", "B")
    }

}