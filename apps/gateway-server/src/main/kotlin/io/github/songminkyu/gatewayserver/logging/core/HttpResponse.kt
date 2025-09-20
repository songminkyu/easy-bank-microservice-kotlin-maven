package io.github.songminkyu.gatewayserver.logging.core

interface HttpResponse : HttpMessage {
    fun status(): Int
}