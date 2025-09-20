package io.github.songminkyu.account.logging.restclient

import io.github.songminkyu.account.logging.core.HttpRequest
import io.github.songminkyu.account.logging.core.Origin
import org.springframework.http.HttpHeaders
import java.net.URI

class RestClientRequest(
    private val request: org.springframework.http.HttpRequest,
    private val requestBody: ByteArray
) : HttpRequest {

    private val uri: URI = request.uri
    private val headers: HttpHeaders = request.headers

    override fun getRemote(): String = "localhost"

    override fun getMethod(): String = request.method.name()

    override fun getRequestUri(): URI = uri

    override fun getOrigin(): Origin = Origin.LOCAL

    override fun headers(): HttpHeaders = headers

    override fun body(): ByteArray? = requestBody
}