package com.mc920.xcommerce.controller

import com.mc920.xcommerce.model.Highlights
import com.mc920.xcommerce.model.Product
import com.mc920.xcommerce.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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
    fun getHighlights(): ResponseEntity<Highlights> {
        val highlights = productService.getHighlights()
        return handleResponse(highlights)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Product> {
        val product = productService.getById(id)
        return handleResponse(product)
    }

    private fun <T> handleResponse(body: T?): ResponseEntity<T> {
        return body?.let { ResponseEntity.ok(it) } ?: return ResponseEntity.notFound().build()
    }

}
