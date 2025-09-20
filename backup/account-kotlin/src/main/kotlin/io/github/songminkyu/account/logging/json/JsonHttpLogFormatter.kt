package io.github.songminkyu.account.logging.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.github.songminkyu.account.logging.core.HttpMessage
import io.github.songminkyu.account.logging.core.StructuredHttpLogFormatter
import io.github.songminkyu.account.logging.utils.HeaderUtils
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*

@Component
class JsonHttpLogFormatter(mapper: ObjectMapper) : StructuredHttpLogFormatter {

    private val om: ObjectMapper = mapper.copy().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
    }

    @Throws(IOException::class)
    override fun prepareBody(message: HttpMessage): Optional<Any> {
        val contentType = message.getContentType()
        val body = message.getBodyAsString()
        if (body.isEmpty()) {
            return Optional.empty()
        }

        return if (contentType != null && HeaderUtils.isHttpMessageInJsonFormat(contentType)) {
            Optional.of(om.readValue(body, Any::class.java))
        } else {
            Optional.of(body)
        }
    }

    @Throws(IOException::class)
    override fun format(content: Map<String, Any?>): String {
        return om.writeValueAsString(content)
    }
}