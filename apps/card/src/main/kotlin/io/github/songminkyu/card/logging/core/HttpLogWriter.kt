package io.github.songminkyu.card.logging.core

import java.io.IOException

interface HttpLogWriter {
    @Throws(IOException::class)
    fun write(payload: String)
}