package io.github.songminkyu.account.logging.utils

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.CollectionUtils
import java.util.*
import java.util.function.Function

object HeaderUtils {

    private val HTTP_MESSAGE_JSON_CONTENT_TYPES = listOf(
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_PROBLEM_JSON_VALUE
    )

    fun toHeaders(feignHeaders: Map<String, Collection<String>>): HttpHeaders {
        val headers = HttpHeaders()
        feignHeaders.keys.forEach { key ->
            headers[key] = getHeaderValues(feignHeaders, key)
        }
        return headers
    }

    fun toHeaders(entries: Iterable<Map.Entry<String, String>>): HttpHeaders {
        val headers = HttpHeaders()
        for (entry in entries) {
            append(headers, entry)
        }
        return headers
    }

    fun toHeadersWithEnumHeaders(
        headerNames: Enumeration<String>,
        getHeaders: Function<String, Enumeration<String>>,
        getHeader: Function<String, String>
    ): HttpHeaders {
        val headers = HttpHeaders()
        while (headerNames.hasMoreElements()) {
            val name = headerNames.nextElement()
            val previous = headers[name]
            if (previous == null) {
                headers[name] = Collections.list(getHeaders.apply(name))
            } else {
                previous.add(getHeader.apply(name))
                headers[name] = previous
            }
        }
        return headers
    }

    fun toHeaders(
        headerNames: Collection<String>,
        getHeaders: Function<String, Collection<String>>,
        getHeader: Function<String, String>
    ): HttpHeaders {
        val headers = HttpHeaders()
        for (name in headerNames) {
            val previous = headers[name]
            if (previous == null) {
                headers[name] = ArrayList(getHeaders.apply(name))
            } else {
                previous.add(getHeader.apply(name))
                headers[name] = previous
            }
        }
        return headers
    }

    private fun getHeaderValues(feignHeaders: Map<String, Collection<String>>, headerName: String): List<String> {
        val values = feignHeaders[headerName]
        return if (!CollectionUtils.isEmpty(values)) {
            ArrayList(values!!)
        } else {
            ArrayList()
        }
    }

    private fun append(headers: HttpHeaders, entry: Map.Entry<String, String>) {
        val previous = headers[entry.key]
        if (previous == null) {
            headers[entry.key] = Collections.singletonList(entry.value)
        } else {
            previous.add(entry.value)
            headers[entry.key] = previous
        }
    }

    fun isHttpMessageInJsonFormat(contentType: String): Boolean {
        return HTTP_MESSAGE_JSON_CONTENT_TYPES.any { contentType.startsWith(it) }
    }
}