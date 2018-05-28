package com.mc851.xcommerce.filters

import com.mc851.xcommerce.service.user.credential.UserCredentialService
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenManager(private val userCredentialService: UserCredentialService) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(!request.requestURI.startsWith("/cart/checkout")){
            addContext(request)
            return true
        }


        val header: String? = request.getHeader("x-auth-token")


        if (header == null) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            return false
        }

        if (!checkAuth(header, request)){
            response.status = HttpStatus.UNAUTHORIZED.value()
            return false
        }

        return true
    }

    private fun checkAuth(header: String, request: HttpServletRequest): Boolean {
        val auth = userCredentialService.verifyToken(header)
        val context = RequestContext(userId = userCredentialService.retrieveUser(header))

        request.setAttribute(RequestContext.CONTEXT, context)

        return auth
    }

    private fun addContext(request: HttpServletRequest) {
        val context = RequestContext()
        request.setAttribute(RequestContext.CONTEXT, context)
    }

}