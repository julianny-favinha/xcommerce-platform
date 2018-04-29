package com.mc920.xcommerce.controller

import com.mc920.xcommerce.model.Highlights
import com.mc920.xcommerce.model.Product
import com.mc920.xcommerce.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("product")
class ProductController {

    @Autowired
    lateinit var productService: ProductService

    @GetMapping("/highlights")
    fun getHighlights(): Highlights {
        return Highlights(highlights = productService.getHighlights())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable(name = "id", required = true) id: Long): Product? {
        return productService.getById(id)
    }

}