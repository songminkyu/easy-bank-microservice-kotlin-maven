package io.github.songminkyu.card.logging.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.github.songminkyu.card.logging.core.HttpMessage
import io.github.songminkyu.card.logging.core.StructuredHttpLogFormatter
import io.github.songminkyu.card.logging.utils.HeaderUtils
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class JsonHttpLogFormatter(mapper: ObjectMapper) : StructuredHttpLogFormatter {
    
    private val om: ObjectMapper = mapper.copy().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
    }
    
    @Throws(IOException::class)
    override fun prepareBody(message: HttpMessage): Any? {
        val contentType = message.getContentType()
        val body = message.getBodyAsObject() ?: message.getBodyAsString()
        
        if (body == null) {
            return null
        }
        
        if (contentType != null && HeaderUtils.isContentTypeSupported(contentType)) {
            if (body is String) {
                return om.readValue(body, Any::class.java)
            }
        }
        return body
    }
    
    @Throws(IOException::class)
    override fun format(content: Map<String, Any>): String {
        return om.writeValueAsString(content)
    }
}