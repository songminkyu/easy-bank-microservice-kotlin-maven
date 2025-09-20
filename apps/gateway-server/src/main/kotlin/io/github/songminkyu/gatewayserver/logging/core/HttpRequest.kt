package io.github.songminkyu.gatewayserver.logging.core

import java.net.URI
import java.util.*

interface HttpRequest : HttpMessage {

    fun getRemote(): String

    fun getMethod(): String

    fun getRequestUri(): URI

    fun getHost(): String {
        return Optional.of(getRequestUri())
            .map { obj: URI -> obj.host }
            .orElse("")
    }

    fun getPath(): String {
        return Optional.of(getRequestUri())
            .map { obj: URI -> obj.path }
            .orElse("")
    }

    fun getQuery(): String {
        return Optional.of(getRequestUri())
            .map { obj: URI -> obj.query }
            .orElse("")
    }
}