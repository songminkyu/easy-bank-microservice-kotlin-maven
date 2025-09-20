package io.github.songminkyu.account.logging.core

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class DefaultHttpLogWriter : HttpLogWriter {

    private val logger = KotlinLogging.logger {}
    
    override fun write(payload: String) {
        logger.info(payload)
    }
}