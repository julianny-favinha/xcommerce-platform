package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.Highlights
import com.mc851.xcommerce.model.Product
import com.mc851.xcommerce.service.LogisticService
import com.mc851.xcommerce.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("logistic")
class LogisticController {

    @Autowired
    lateinit var logisticService: LogisticService

    @GetMapping("/shipping")
    fun getHighlights(): ResponseEntity<Highlights> {
        val highlights = productService.getHighlights()
        return handleResponse(highlights)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Product> {
        val product = productService.getById(id)
        return handleResponse(product)
    }

}
