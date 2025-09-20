package io.github.songminkyu.account.logging.webclient

import io.github.songminkyu.account.logging.core.HttpRequest
import io.github.songminkyu.account.logging.core.Origin
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.ClientRequest as SpringClientRequest
import java.net.URI

internal class ClientRequest(
    private val request: SpringClientRequest
) : HttpRequest {

    private val uri: URI = request.url()
    private val headers: HttpHeaders = request.headers()
    private var requestBody: ByteArray? = null

    override fun getRemote(): String = "localhost"

    override fun getMethod(): String = request.method().name()

    override fun getRequestUri(): URI = uri

    override fun getOrigin(): Origin = Origin.LOCAL

    override fun headers(): HttpHeaders = headers

    fun shouldBuffer(): Boolean = true

    fun buffer(message: ByteArray) {
        this.requestBody = message
    }

    override fun body(): ByteArray? = requestBody
}