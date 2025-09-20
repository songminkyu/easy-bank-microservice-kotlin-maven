package io.github.songminkyu.account.logging.webclient

import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import java.util.function.Consumer

internal object DataBufferCopyUtils {

    fun wrapAndBuffer(
        body: Publisher<out DataBuffer>,
        copyConsumer: Consumer<ByteArray>
    ): Publisher<out DataBuffer> =
        DataBufferUtils
            .join(body)
            .defaultIfEmpty(DefaultDataBufferFactory.sharedInstance.wrap(ByteArray(0)))
            .map { dataBuffer ->
                val bytes = ByteArray(dataBuffer.readableByteCount())
                dataBuffer.read(bytes)
                DataBufferUtils.release(dataBuffer)
                val wrappedDataBuffer = DefaultDataBufferFactory.sharedInstance.wrap(bytes)
                copyConsumer.accept(bytes)
                wrappedDataBuffer
            }
}