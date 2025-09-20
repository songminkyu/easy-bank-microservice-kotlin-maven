package io.github.songminkyu.card.logging.core

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
    fun format(content: Map<String, Any>): String

    @Throws(IOException::class)
    fun prepare(request: HttpRequest): Map<String, Any> {
        val content = LinkedHashMap<String, Any>()

        content["origin"] = request.getOrigin().name.lowercase(Locale.ROOT)
        content["type"] = "request"
        content["protocol"] = request.getProtocolVersion()
        content["remote"] = request.getRemote()
        content["method"] = request.getMethod()
        content["uri"] = request.getRequestUri()
        content["host"] = request.getHost()
        content["path"] = request.getPath()

        prepareHeaders(request)?.let { content["headers"] = it }
        if (request.getAdditionalContent().isNotEmpty()) {
            content.putAll(request.getAdditionalContent())
        }
        prepareBody(request)?.let { content["body"] = it }
        content["path"] = request.getPath()
        return content
    }

    @Throws(IOException::class)
    fun prepare(response: HttpResponse): Map<String, Any> {
        val content = LinkedHashMap<String, Any>()

        content["origin"] = response.getOrigin().name.lowercase(Locale.ROOT)
        content["type"] = "response"
        content["protocol"] = response.getProtocolVersion()
        content["status"] = response.status()

        prepareHeaders(response)?.let { content["headers"] = it }
        if (response.getAdditionalContent().isNotEmpty()) {
            content.putAll(response.getAdditionalContent())
        }
        prepareBody(response)?.let { content["body"] = it }

        return content
    }

    fun prepareHeaders(message: HttpMessage): HttpHeaders? {
        val headers = message.headers()
        return if (headers.isEmpty()) null else headers
    }

    @Throws(IOException::class)
    fun prepareBody(message: HttpMessage): Any? {
        val body = message.getBodyAsString()
        return if (body.isEmpty()) null else body
    }
}