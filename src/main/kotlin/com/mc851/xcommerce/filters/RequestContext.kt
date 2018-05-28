package com.mc851.xcommerce.filters

import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

data class RequestContext(val userId: Long?) {
    constructor(): this(null)
    companion object {
        const val CONTEXT = "_CONTEXT"
    }
}

class ContextFilter(): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.setAttribute(RequestContext.CONTEXT, RequestContext())
        return super.preHandle(request, response, handler)
    }
}