package io.github.songminkyu.account.logging.feign

import feign.Request
import io.github.songminkyu.account.logging.core.HttpRequest
import io.github.songminkyu.account.logging.core.Origin
import io.github.songminkyu.account.logging.utils.HeaderUtils
import org.springframework.http.HttpHeaders
import java.net.URI

data class LocalRequest(
    private val uri: URI,
    private val httpMethod: Request.HttpMethod,
    private val headers: HttpHeaders,
    private val body: ByteArray?
) : HttpRequest {

    companion object {
        fun create(request: Request): LocalRequest {
            return LocalRequest(
                URI.create(request.url()),
                request.httpMethod(),
                HeaderUtils.toHeaders(request.headers()),
                request.body()
            )
        }
    }

    override fun getRemote(): String = "localhost"

    override fun getMethod(): String = httpMethod.toString()

    override fun getRequestUri(): URI = uri

    override fun getProtocolVersion(): String = "HTTP/1.1"

    override fun getOrigin(): Origin = Origin.LOCAL

    override fun headers(): HttpHeaders = headers

    override fun body(): ByteArray = body ?: byteArrayOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocalRequest

        if (uri != other.uri) return false
        if (httpMethod != other.httpMethod) return false
        if (headers != other.headers) return false
        if (body != null) {
            if (other.body == null) return false
            if (!body.contentEquals(other.body)) return false
        } else if (other.body != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + httpMethod.hashCode()
        result = 31 * result + headers.hashCode()
        result = 31 * result + (body?.contentHashCode() ?: 0)
        return result
    }
}