package io.github.songminkyu.gatewayserver.logging.core

import mu.KLogging
import org.springframework.stereotype.Component

@Component
class DefaultHttpLogWriter : HttpLogWriter {
    
    companion object : KLogging()
    
    override fun write(payload: String) {
        logger.info(payload)
    }
}