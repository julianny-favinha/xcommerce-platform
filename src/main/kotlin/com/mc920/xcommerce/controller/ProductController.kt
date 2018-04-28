package com.mc920.xcommerce.controller

import com.mc920.xcommerce.model.Products
import com.mc920.xcommerce.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("product")
class ProductController {

    @Autowired
    lateinit var productService: ProductService

    @GetMapping("/highlights")
    fun getHighlights(): Products {
        return Products(productService.getHighlights())
    }

}