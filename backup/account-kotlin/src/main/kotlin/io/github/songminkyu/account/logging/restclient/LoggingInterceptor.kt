package io.github.songminkyu.account.logging.restclient

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.songminkyu.account.logging.core.HttpResponse
import io.github.songminkyu.account.logging.core.Sink
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.io.IOException
import java.io.UncheckedIOException

private val logger = KotlinLogging.logger {}

class LoggingInterceptor(
    private val sink: Sink
) : ClientHttpRequestInterceptor {

    override fun intercept(
        clientRequest: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        return try {
            logRequest(clientRequest, body)
            val response = execution.execute(clientRequest, body)
            logResponse(response)
            response
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }

    @Throws(IOException::class)
    private fun logRequest(request: HttpRequest, body: ByteArray) {
        val httpRequest = RestClientRequest(request, body)
        sink.write(httpRequest)
    }

    @Throws(IOException::class)
    private fun logResponse(response: ClientHttpResponse) {
        val clientResponse: HttpResponse = RestClientResponse(response)
        sink.write(clientResponse)
    }
}