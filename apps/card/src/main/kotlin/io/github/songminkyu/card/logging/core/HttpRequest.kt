package io.github.songminkyu.card.logging.core

import java.net.URI

interface HttpRequest : HttpMessage {
    fun getRemote(): String

    fun getMethod(): String

    fun getRequestUri(): URI

    fun getHost(): String {
        return getRequestUri().host ?: ""
    }

    fun getPath(): String {
        return getRequestUri().path ?: ""
    }

    fun getQuery(): String {
        return getRequestUri().query ?: ""
    }

    fun getAdditionalContent(): Map<String, Any> {
        return emptyMap()
    }
}