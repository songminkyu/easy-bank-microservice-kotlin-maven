package io.github.songminkyu.card.exception.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class SecurityProblemSupport(
    private val handlerExceptionResolver: HandlerExceptionResolver
) : AuthenticationEntryPoint, AccessDeniedHandler {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        ex: AuthenticationException
    ) {
        handlerExceptionResolver.resolveException(request, response, null, ex)
    }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        ex: AccessDeniedException
    ) {
        handlerExceptionResolver.resolveException(request, response, null, ex)
    }
}