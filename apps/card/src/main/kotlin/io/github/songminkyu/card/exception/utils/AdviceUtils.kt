package io.github.songminkyu.card.exception.utils

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import java.io.IOException

object AdviceUtils {

    @Throws(IOException::class)
    fun setHttpResponse(
        entity: ResponseEntity<ProblemDetail>,
        response: HttpServletResponse,
        mapper: ObjectMapper
    ) {
        response.status = entity.statusCode.value()
        val out = response.writer
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        out.print(mapper.writeValueAsString(entity.body))
        out.flush()
    }
}