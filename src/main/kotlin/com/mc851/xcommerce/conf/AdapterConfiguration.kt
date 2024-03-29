package com.mc851.xcommerce.conf

import com.mc851.xcommerce.filters.ContextFilter
import com.mc851.xcommerce.filters.RequestContext
import com.mc851.xcommerce.filters.TokenManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest

@Configuration
class AdapterConfiguration : WebMvcConfigurer {

    @Autowired
    lateinit var tokenManager: TokenManager

    @Autowired
    lateinit var requestFilter: ContextFilter

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(requestFilter)
        registry.addInterceptor(tokenManager).addPathPatterns("/cart/checkout/**", "/user", "/user/update", "/order/**", "/order", "/sac/**")
        super.addInterceptors(registry)
    }
}

@ControllerAdvice
class ModelAttributeControllerAdvice {

    @ModelAttribute(RequestContext.CONTEXT)
    fun getRequestContext(request: HttpServletRequest): RequestContext {
        return request.getAttribute(RequestContext.CONTEXT) as RequestContext
    }

}