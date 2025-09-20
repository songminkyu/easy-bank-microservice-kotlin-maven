package io.github.songminkyu.account.logging.webclient

import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.client.reactive.ClientHttpRequest
import org.springframework.http.client.reactive.ClientHttpRequestDecorator
import reactor.core.publisher.Mono

@Suppress("NullableProblems")
internal class BufferingClientHttpRequest(
    delegate: ClientHttpRequest,
    private val clientRequest: ClientRequest
) : ClientHttpRequestDecorator(delegate) {

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> =
        super.writeWith(bufferingWrap(body))

    private fun bufferingWrap(body: Publisher<out DataBuffer>): Publisher<out DataBuffer> =
        if (clientRequest.shouldBuffer()) {
            DataBufferCopyUtils.wrapAndBuffer(body, clientRequest::buffer)
        } else {
            body
        }
}