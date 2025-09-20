package io.github.songminkyu.gatewayserver.logging.core

import java.io.IOException

interface HttpLogWriter {

    @Throws(IOException::class)
    fun write(payload: String)
}