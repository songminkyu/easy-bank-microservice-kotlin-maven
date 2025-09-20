package io.github.songminkyu.account.logging.feign

import feign.Response
import io.github.songminkyu.account.logging.core.HttpResponse
import io.github.songminkyu.account.logging.core.Origin
import io.github.songminkyu.account.logging.utils.HeaderUtils
import org.springframework.http.HttpHeaders

data class RemoteResponse(
    private val status: Int,
    private val headers: HttpHeaders,
    private val body: ByteArray?
) : HttpResponse {

    companion object {
        fun create(response: Response, body: ByteArray?): RemoteResponse {
            return RemoteResponse(
                response.status(),
                HeaderUtils.toHeaders(response.headers()),
                body
            )
        }
    }

    override fun status(): Int = status

    override fun getProtocolVersion(): String = "HTTP/1.1"

    override fun getOrigin(): Origin = Origin.REMOTE

    override fun headers(): HttpHeaders = headers

    override fun body(): ByteArray = body ?: byteArrayOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RemoteResponse

        if (status != other.status) return false
        if (headers != other.headers) return false
        if (body != null) {
            if (other.body == null) return false
            if (!body.contentEquals(other.body)) return false
        } else if (other.body != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status
        result = 31 * result + headers.hashCode()
        result = 31 * result + (body?.contentHashCode() ?: 0)
        return result
    }
}