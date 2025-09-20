package io.github.songminkyu.card.logging.core

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DefaultHttpLogWriter : HttpLogWriter {
    companion object {
        private val log = LoggerFactory.getLogger(DefaultHttpLogWriter::class.java)
    }

    override fun write(payload: String) {
        log.info(payload)
    }
}