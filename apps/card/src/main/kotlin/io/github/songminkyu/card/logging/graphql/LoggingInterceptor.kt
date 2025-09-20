package io.github.songminkyu.card.logging.graphql

import io.github.songminkyu.card.exception.fauxpas.FauxPas.throwingRunnable
import io.github.songminkyu.card.logging.core.Sink
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import reactor.core.publisher.Mono

class LoggingInterceptor(
    private val sink: Sink
) : WebGraphQlInterceptor {
    
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        return chain.next(request).doOnNext { response ->
            val graphQlRequest = RemoteRequest(request)
            val graphQlResponse = LocalResponse(response)
            throwingRunnable<Exception> {
                sink.write(graphQlRequest)
                sink.write(graphQlResponse)
            }.run()
        }
    }
}