package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.CreditCardInfo
import com.mc851.xcommerce.model.CreditCardPayment
import com.mc851.xcommerce.model.Highlights
import com.mc851.xcommerce.model.PaymentResult
import com.mc851.xcommerce.model.Product
import com.mc851.xcommerce.model.Search
import com.mc851.xcommerce.model.UserInfo
import com.mc851.xcommerce.service.PaymentService
import com.mc851.xcommerce.service.ProductService
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

    @Autowired
    lateinit var paymentService: PaymentService

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

    @GetMapping("/reserve")
    fun reserve(): ResponseEntity<Boolean> {
        val product: Map<Product, Int> = mapOf(
                Product(
                2,
                "Nome",
                "Marca",
                100,
                2L,
                1L,
                1L,
                1L,
                "Categoria",
                "Descrição",
                "") to 2)

        val result = productService.reserveProducts(product)

        return handleResponse(result)
    }

    @GetMapping("/release")
    fun release(): ResponseEntity<Boolean> {
        val product: Map<Product, Int> = mapOf(
                Product(
                2,
                "Nome",
                "Marca",
                100,
                2L,
                1L,
                1L,
                1L,
                "Categoria",
                "Descrição",
                "") to 2)

        val result = productService.releaseProducts(product)

        return handleResponse(result)
    }

}
