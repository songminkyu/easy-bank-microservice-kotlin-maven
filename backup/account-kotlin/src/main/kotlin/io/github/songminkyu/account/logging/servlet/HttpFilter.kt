package io.github.songminkyu.account.logging.servlet

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException

interface HttpFilter : Filter {

    override fun init(filterConfig: FilterConfig?) {
        // no initialization needed by default
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpRequest = request as? HttpServletRequest
            ?: throw IllegalArgumentException("${javaClass.simpleName} only supports HTTP")
        val httpResponse = response as? HttpServletResponse
            ?: throw IllegalArgumentException("${javaClass.simpleName} only supports HTTP")

        doFilter(httpRequest, httpResponse, chain)
    }

    @Throws(ServletException::class, IOException::class)
    fun doFilter(
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse,
        chain: FilterChain
    )

    override fun destroy() {
        // no deconstruction needed by default
    }
}