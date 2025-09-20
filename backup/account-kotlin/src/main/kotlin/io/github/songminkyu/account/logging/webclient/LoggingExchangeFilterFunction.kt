package io.github.songminkyu.account.logging.webclient

import io.github.songminkyu.account.exception.fauxpas.FauxPas.throwingConsumer
import io.github.songminkyu.account.exception.fauxpas.FauxPas.throwingFunction
import io.github.songminkyu.account.logging.core.Sink
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpHeaders.TRANSFER_ENCODING
import org.springframework.web.reactive.function.client.ClientRequest as SpringClientRequest
import org.springframework.web.reactive.function.client.ClientResponse as SpringClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Suppress("NullableProblems")
class LoggingExchangeFilterFunction(
    private val sink: Sink
) : ExchangeFilterFunction {

    override fun filter(
        request: SpringClientRequest,
        next: ExchangeFunction
    ): Mono<SpringClientResponse> {
        val clientRequest = ClientRequest(request)
        return next
            .exchange(
                SpringClientRequest
                    .from(request)
                    .body { outputMessage, context ->
                        request.body()
                            .insert(BufferingClientHttpRequest(outputMessage, clientRequest), context)
                    }
                    .build()
            )
            .flatMap { response ->
                sink.write(clientRequest)
                val clientResponse = ClientResponse(response)
                Mono
                    .just(response)
                    .flatMap { responseRef ->
                        val responseHeaders = response.headers().asHttpHeaders()
                        if (clientResponse.shouldBuffer() &&
                            (responseHeaders.contentLength > 0 || responseHeaders.containsKey(TRANSFER_ENCODING))) {
                            responseRef
                                .bodyToMono(ByteArray::class.java)
                                .doOnNext(clientResponse::buffer)
                                .map { bytes ->
                                    response.mutate()
                                        .body(Flux.just(DefaultDataBufferFactory.sharedInstance.wrap(bytes)))
                                        .build()
                                }
                                .switchIfEmpty(Mono.just(responseRef))
                        } else {
                            Mono.just(responseRef)
                        }
                    }
                    .doOnNext { sink.write(clientResponse) }
            }
    }
}