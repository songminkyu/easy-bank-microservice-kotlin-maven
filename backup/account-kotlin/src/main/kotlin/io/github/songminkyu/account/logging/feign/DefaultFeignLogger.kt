package io.github.songminkyu.account.logging.feign

import feign.Logger
import feign.Request
import feign.Response
import io.github.songminkyu.account.logging.core.HttpRequest
import io.github.songminkyu.account.logging.core.HttpResponse
import io.github.songminkyu.account.logging.core.Sink
import io.github.songminkyu.account.logging.utils.ByteStreams
import java.io.IOException
import java.io.UncheckedIOException

class DefaultFeignLogger(private val sink: Sink) : Logger() {

    override fun log(configKey: String, format: String, vararg args: Any?) {
        /* no-op, logging is delegated */
    }

    override fun logRetry(configKey: String, logLevel: Level) {
        /* no-op, logging is delegated */
    }

    override fun logIOException(
        configKey: String,
        logLevel: Level,
        ioe: IOException,
        elapsedTime: Long
    ): IOException {
        /* no-op, logging is delegated */
        return ioe
    }

    override fun logRequest(configKey: String, logLevel: Level, request: Request) {
        val httpRequest: HttpRequest = LocalRequest.create(request)
        try {
            sink.write(httpRequest)
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }

    override fun logAndRebufferResponse(
        configKey: String,
        logLevel: Level,
        response: Response,
        elapsedTime: Long
    ): Response {
        return try {
            val body = response.body()?.let { ByteStreams.toByteArray(it.asInputStream()) }
            
            val httpResponse: HttpResponse = RemoteResponse.create(response, body)
            sink.write(httpResponse)
            
            Response.builder()
                .status(response.status())
                .request(response.request())
                .reason(response.reason())
                .headers(response.headers())
                .body(body)
                .build()
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }
}