package com.mc920.xcommerce.service

import com.mc920.xcommerce.clients.product.ProductClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService(val productClient: ProductClient) {

    @Autowired
    private val productClient: ProductClient

}