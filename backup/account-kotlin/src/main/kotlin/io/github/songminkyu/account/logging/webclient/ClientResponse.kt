package io.github.songminkyu.account.logging.webclient

import io.github.songminkyu.account.logging.core.HttpResponse
import io.github.songminkyu.account.logging.core.Origin
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.ClientResponse as SpringClientResponse

internal class ClientResponse(
    private val response: SpringClientResponse
) : HttpResponse {

    private val headers: HttpHeaders = response.headers().asHttpHeaders()
    private var responseBody: ByteArray? = null

    override fun status(): Int = response.statusCode().value()

    override fun getOrigin(): Origin = Origin.REMOTE

    override fun headers(): HttpHeaders = headers

    fun shouldBuffer(): Boolean = true

    fun buffer(message: ByteArray) {
        this.responseBody = message
    }

    override fun body(): ByteArray? = responseBody
}