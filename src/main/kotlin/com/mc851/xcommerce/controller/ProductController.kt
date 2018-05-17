package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.Highlights
import com.mc851.xcommerce.model.Product
import com.mc851.xcommerce.model.Search
import com.mc851.xcommerce.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PostMapping("/search")
    fun search(@RequestBody text: String): ResponseEntity<Search> {
        val result = productService.search(text)
        return handleResponse(result)
    }
}
