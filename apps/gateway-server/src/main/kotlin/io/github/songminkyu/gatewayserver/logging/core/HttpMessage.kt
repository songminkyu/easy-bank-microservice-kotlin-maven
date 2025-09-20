package io.github.songminkyu.gatewayserver.logging.core

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

interface HttpMessage {

    fun getProtocolVersion(): String = "HTTP/1.1"

    fun getOrigin(): Origin

    fun headers(): HttpHeaders

    fun getContentType(): String? {
        return Optional.ofNullable(headers().contentType)
            .map { obj: MediaType -> obj.toString() }
            .orElse(null)
    }

    @Throws(IOException::class)
    fun body(): ByteArray?

    @Throws(IOException::class)
    fun getBodyAsString(): String {
        return Optional.ofNullable(body())
            .map { bytes: ByteArray -> String(bytes, StandardCharsets.UTF_8) }
            .orElse("")
    }
}