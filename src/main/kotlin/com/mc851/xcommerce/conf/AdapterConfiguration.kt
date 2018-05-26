package com.mc851.xcommerce.conf

import com.mc851.xcommerce.filters.TokenManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AdapterConfiguration : WebMvcConfigurer {

    @Autowired
    lateinit var tokenManager: TokenManager

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(tokenManager).addPathPatterns("/checkout/**")
        super.addInterceptors(registry)
    }
}