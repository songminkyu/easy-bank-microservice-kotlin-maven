package io.github.songminkyu.account.logging.servlet

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import java.io.ByteArrayInputStream
import java.io.IOException

internal class ServletInputStreamAdapter(
    private val stream: ByteArrayInputStream
) : ServletInputStream() {

    override fun read(): Int = stream.read()

    @Throws(IOException::class)
    override fun read(b: ByteArray): Int = stream.read(b)

    override fun read(b: ByteArray, off: Int, len: Int): Int = stream.read(b, off, len)

    override fun isFinished(): Boolean = stream.available() == 0

    override fun isReady(): Boolean = true

    override fun setReadListener(readListener: ReadListener) {
        throw UnsupportedOperationException()
    }
}