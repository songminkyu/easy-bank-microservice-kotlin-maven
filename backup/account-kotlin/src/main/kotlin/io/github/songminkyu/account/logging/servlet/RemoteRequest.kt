package io.github.songminkyu.account.logging.servlet

import io.github.songminkyu.account.logging.core.HttpRequest
import io.github.songminkyu.account.logging.core.Origin
import io.github.songminkyu.account.logging.utils.ByteStreams
import io.github.songminkyu.account.logging.utils.HeaderUtils
import jakarta.servlet.AsyncContext
import jakarta.servlet.AsyncListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.http.HttpHeaders
import org.springframework.http.server.ServletServerHttpRequest
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.URI
import java.nio.charset.StandardCharsets

class RemoteRequest(
    request: HttpServletRequest
) : HttpServletRequestWrapper(request), HttpRequest {

    private val asyncListener: AsyncListener? = null
    private val headers: HttpHeaders
    private val requestBody: ByteArray
    private val uri: URI

    init {
        headers = HeaderUtils.toHeadersWithEnumHeaders(
            request.headerNames,
            request::getHeaders,
            request::getHeader
        )
        requestBody = ByteStreams.toByteArray(request.inputStream)
        uri = ServletServerHttpRequest(request).uri
    }

    override fun getProtocolVersion(): String = protocol

    override fun getOrigin(): Origin = Origin.REMOTE

    override fun getRemote(): String = remoteAddr

    override fun getRequestUri(): URI = uri

    override fun headers(): HttpHeaders = headers

    override fun getContentType(): String? {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream =
        ServletInputStreamAdapter(ByteArrayInputStream(requestBody))

    @Throws(IOException::class)
    override fun getReader(): BufferedReader {
        val stream = inputStream
        val reader = InputStreamReader(stream, StandardCharsets.UTF_8)
        return BufferedReader(reader)
    }

    override fun body(): ByteArray? = requestBody

    @Throws(IllegalStateException::class)
    override fun startAsync(): AsyncContext {
        val asyncContext = super.startAsync()
        asyncListener?.let { asyncContext.addListener(it) }
        return asyncContext
    }

    @Throws(IllegalStateException::class)
    override fun startAsync(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse
    ): AsyncContext {
        val asyncContext = super.startAsync(servletRequest, servletResponse)
        asyncListener?.let { asyncContext.addListener(it) }
        return asyncContext
    }
}