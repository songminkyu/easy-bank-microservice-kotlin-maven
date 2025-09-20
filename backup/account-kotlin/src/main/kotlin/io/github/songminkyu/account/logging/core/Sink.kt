package io.github.songminkyu.account.logging.core

import java.io.IOException

interface Sink {

    @Throws(IOException::class)
    fun write(request: HttpRequest)

    @Throws(IOException::class)
    fun write(response: HttpResponse)
}