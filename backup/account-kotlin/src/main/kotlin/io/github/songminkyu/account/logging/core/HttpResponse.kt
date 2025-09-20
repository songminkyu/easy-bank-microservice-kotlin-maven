package io.github.songminkyu.account.logging.core

interface HttpResponse : HttpMessage {
    fun status(): Int
}