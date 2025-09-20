package io.github.songminkyu.card.logging.core

import java.io.IOException

interface HttpLogFormatter {
    @Throws(IOException::class)
    fun format(request: HttpRequest): String

    @Throws(IOException::class)
    fun format(response: HttpResponse): String
}