package com.mc920.xcommerce.conf

import com.mc920.xcommerce.clients.product.ProductClient
import com.mc920.xcommerce.clients.product.ProductClientOkHttp
import com.mc920.xcommerce.service.ProductService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun productService(productClient: ProductClient): ProductService {
        return ProductService(productClient)
    }

    @Bean
    fun productClient(): ProductClient {
        return ProductClientOkHttp()
    }
}