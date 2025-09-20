package io.github.songminkyu.gatewayserver.logging.core

import org.springframework.http.HttpHeaders
import java.io.IOException
import java.util.*

interface StructuredHttpLogFormatter : HttpLogFormatter {

    @Throws(IOException::class)
    override fun format(response: HttpResponse): String {
        return format(prepare(response))
    }

    @Throws(IOException::class)
    override fun format(request: HttpRequest): String {
        return format(prepare(request))
    }

    @Throws(IOException::class)
    fun format(content: Map<String, Any?>): String

    @Throws(IOException::class)
    fun prepare(request: HttpRequest): Map<String, Any?> {
        val content = LinkedHashMap<String, Any?>()
        
        content["origin"] = request.getOrigin().name.lowercase(Locale.ROOT)
        content["type"] = "request"
        content["protocol"] = request.getProtocolVersion()
        content["remote"] = request.getRemote()
        content["method"] = request.getMethod()
        content["uri"] = request.getRequestUri()
        content["host"] = request.getHost()
        content["path"] = request.getPath()
        
        prepareHeaders(request).ifPresent { headers -> content["headers"] = headers }
        prepareBody(request).ifPresent { body -> content["body"] = body }
        
        return content
    }

    @Throws(IOException::class)
    fun prepare(response: HttpResponse): Map<String, Any?> {
        val content = LinkedHashMap<String, Any?>()
        
        content["origin"] = response.getOrigin().name.lowercase(Locale.ROOT)
        content["type"] = "response"
        content["protocol"] = response.getProtocolVersion()
        content["status"] = response.status()
        
        prepareHeaders(response).ifPresent { headers -> content["headers"] = headers }
        prepareBody(response).ifPresent { body -> content["body"] = body }
        
        return content
    }

    fun prepareHeaders(message: HttpMessage): Optional<HttpHeaders> {
        val headers = message.headers()
        return Optional.ofNullable(if (headers.isEmpty()) null else headers)
    }

    @Throws(IOException::class)
    fun prepareBody(message: HttpMessage): Optional<Any?> {
        val body = message.getBodyAsString()
        return Optional.ofNullable(if (body.isEmpty()) null else body)
    }
}