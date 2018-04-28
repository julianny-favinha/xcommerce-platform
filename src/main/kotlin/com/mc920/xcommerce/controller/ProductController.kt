package com.mc920.xcommerce.controller

import com.mc920.xcommerce.model.Product
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("product")
class ProductController {

    @GetMapping("/highlights")
    fun getHighlights() : List<Product> {
        TODO()
    }


}