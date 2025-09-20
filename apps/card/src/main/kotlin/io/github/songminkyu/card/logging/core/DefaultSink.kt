package io.github.songminkyu.card.logging.core

import org.springframework.stereotype.Component
import java.io.IOException

@Component
class DefaultSink(
    private val formatter: HttpLogFormatter,
    private val writer: HttpLogWriter
) : Sink {

    @Throws(IOException::class)
    override fun write(request: HttpRequest) {
        writer.write(formatter.format(request))
    }

    @Throws(IOException::class)
    override fun write(response: HttpResponse) {
        writer.write(formatter.format(response))
    }
}