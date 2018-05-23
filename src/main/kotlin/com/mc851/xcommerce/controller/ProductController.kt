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
        val product: Map<Product, Int> = mapOf(Product(2, "Nome", "Marca", 100, "Categoria", "Descricao", "") to 2)

        val result = productService.reserveProducts(product)

        return handleResponse(result)
    }

    @GetMapping("/release")
    fun release(): ResponseEntity<Boolean> {
        val product: Map<Product, Int> = mapOf(Product(2, "Nome", "Marca", 100, "Categoria", "Descricao", "") to 2)

        val result = productService.releaseProducts(product)

        return handleResponse(result)
    }

    @GetMapping("/test")
    fun pay(): Boolean {

        val creditCardPayment = CreditCardPayment(CreditCardInfo("IGOR", 2L, 2017L, 737, 1111222233334444),
            UserInfo("39392645805", "Igor", "Rua Augusto", "13465700"),
            1000L,
            0L)
        val result = paymentService.payCreditCard(creditCardPayment)
        if (result == PaymentResult.AUTHORIZED)
            return true
        return false
    }
}
