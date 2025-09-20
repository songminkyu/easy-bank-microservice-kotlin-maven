package io.github.songminkyu.account.logging.servlet

import io.github.songminkyu.account.logging.core.Sink
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException

class LoggingFilter(
    private val sink: Sink
) : HttpFilter {

    @Throws(ServletException::class, IOException::class)
    override fun doFilter(
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse,
        chain: FilterChain
    ) {
        val request = RemoteRequest(httpRequest)
        val response = LocalResponse(httpResponse, request.getProtocolVersion())

        chain.doFilter(request, response)

        write(request, response)
    }

    @Throws(IOException::class)
    private fun write(request: RemoteRequest, response: LocalResponse) {
        sink.write(request)
        response.flushBuffer()
        sink.write(response)
    }
}