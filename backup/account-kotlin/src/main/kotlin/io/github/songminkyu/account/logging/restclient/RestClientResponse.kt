package io.github.songminkyu.account.logging.restclient

import io.github.songminkyu.account.logging.core.HttpResponse
import io.github.songminkyu.account.logging.core.Origin
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.ClientHttpResponse
import java.io.IOException

class RestClientResponse(
    private val response: ClientHttpResponse
) : HttpResponse {

    private val headers: HttpHeaders = response.headers
    private val responseBody: ByteArray
    private val status: HttpStatusCode = response.statusCode

    init {
        responseBody = try {
            response.body.readAllBytes()
        } catch (e: IOException) {
            throw e
        }
    }

    override fun status(): Int = status.value()

    override fun getOrigin(): Origin = Origin.REMOTE

    override fun headers(): HttpHeaders = headers

    override fun body(): ByteArray? = responseBody
}