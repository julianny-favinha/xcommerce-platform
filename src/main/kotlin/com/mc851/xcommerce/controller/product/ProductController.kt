package com.mc851.xcommerce.controller.product

import com.mc851.xcommerce.controller.utils.handleResponse
import com.mc851.xcommerce.model.api.Highlights
import com.mc851.xcommerce.model.api.Product
import com.mc851.xcommerce.model.api.Search
import com.mc851.xcommerce.model.api.SearchIn
import com.mc851.xcommerce.service.product.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @PostMapping("/search")
    fun search(@RequestBody text: SearchIn): ResponseEntity<Search> {
        val result = productService.search(text.query)
        return handleResponse(result)
    }

}
