package io.github.songminkyu.card.logging.core

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.io.IOException
import java.nio.charset.StandardCharsets

interface HttpMessage {
    fun getProtocolVersion(): String = "HTTP/1.1"

    fun getOrigin(): Origin

    fun headers(): HttpHeaders

    fun getContentType(): String? {
        return headers().contentType?.toString()
    }

    @Throws(IOException::class)
    fun body(): ByteArray?

    @Throws(IOException::class)
    fun getBodyAsObject(): Any? = getBodyAsString()

    @Throws(IOException::class)
    fun getBodyAsString(): String {
        return body()?.let { String(it, StandardCharsets.UTF_8) } ?: ""
    }
}