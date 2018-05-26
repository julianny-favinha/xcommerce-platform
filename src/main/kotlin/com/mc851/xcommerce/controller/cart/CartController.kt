package com.mc851.xcommerce.controller.cart

import com.mc851.xcommerce.controller.utils.handleResponse
import com.mc851.xcommerce.model.api.Cart
import com.mc851.xcommerce.model.api.CartItem
import com.mc851.xcommerce.service.product.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("cart")
class CartController {

    @Autowired
    lateinit var productService: ProductService

    @PostMapping("/reserve")
    fun reserve(@RequestBody request: CartItem): ResponseEntity<Boolean> {
        val result = productService.reserveProducts(mapOf(request.product to request.quantity))
        return handleResponse(result)
    }

    @PostMapping("/full_reserve")
    fun fullReserve(@RequestBody request: Cart): ResponseEntity<Boolean> {
        val products = request.cartItems.associateBy({ it.product }, { it.quantity })
        val result = productService.reserveProducts(products)
        return handleResponse(result)
    }

    @PostMapping("/release")
    fun release(@RequestBody request: CartItem): ResponseEntity<Boolean> {
        val result = productService.releaseProducts(mapOf(request.product to request.quantity))
        return handleResponse(result)
    }

    @PostMapping("/full_release")
    fun fullRelease(@RequestBody request: Cart): ResponseEntity<Boolean> {
        val products = request.cartItems.associateBy({ it.product }, { it.quantity })
        val result = productService.reserveProducts(products)
        return handleResponse(result)
    }
}
