package io.github.songminkyu.card.logging.core

interface HttpResponse : HttpMessage {
    fun status(): Int

    fun getAdditionalContent(): Map<String, Any> {
        return emptyMap()
    }
}