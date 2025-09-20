package io.github.songminkyu.account.logging.servlet

import io.github.songminkyu.account.logging.core.HttpResponse
import io.github.songminkyu.account.logging.core.Origin
import io.github.songminkyu.account.logging.utils.HeaderUtils
import jakarta.servlet.ServletOutputStream
import jakarta.servlet.WriteListener
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import org.springframework.http.HttpHeaders
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.function.Supplier

class LocalResponse(
    response: HttpServletResponse,
    private val protocolVersion: String
) : HttpServletResponseWrapper(response), HttpResponse {

    private val headers: HttpHeaders = HeaderUtils.toHeaders(
        response.headerNames,
        response::getHeaders,
        response::getHeader
    )

    private val tee: Tee = Tee(response.outputStream)

    override fun getOrigin(): Origin = Origin.LOCAL

    override fun getProtocolVersion(): String = protocolVersion

    override fun headers(): HttpHeaders = headers

    override fun getContentType(): String? {
        TODO("Not yet implemented")
    }

    override fun status(): Int = status

    @Throws(IOException::class)
    override fun getOutputStream(): ServletOutputStream = tee.getOutputStream()

    @Throws(IOException::class)
    override fun getWriter(): PrintWriter = tee.getWriter { StandardCharsets.UTF_8 }

    @Throws(IOException::class)
    override fun flushBuffer() {
        tee.flush()
        super.flushBuffer()
    }

    override fun body(): ByteArray? = tee.getBytes()

    private class Tee(original: ServletOutputStream) {
        private val branch = ByteArrayOutputStream()
        private val output = TeeServletOutputStream(original, branch)
        private var writer: PrintWriter? = null
        private var bytes: ByteArray? = null

        fun getOutputStream(): ServletOutputStream = output

        fun getWriter(charset: Supplier<Charset>): PrintWriter {
            if (writer == null) {
                writer = PrintWriter(OutputStreamWriter(output, charset.get()))
            }
            return writer!!
        }

        @Throws(IOException::class)
        fun flush() {
            writer?.flush() ?: output.flush()
        }

        fun getBytes(): ByteArray {
            if (bytes == null) {
                bytes = branch.toByteArray()
            }
            return bytes!!
        }
    }

    private class TeeServletOutputStream(
        private val original: ServletOutputStream,
        private val branch: OutputStream
    ) : ServletOutputStream() {

        @Throws(IOException::class)
        override fun write(b: Int) {
            original.write(b)
            branch.write(b)
        }

        @Throws(IOException::class)
        override fun write(b: ByteArray, off: Int, len: Int) {
            original.write(b, off, len)
            branch.write(b, off, len)
        }

        @Throws(IOException::class)
        override fun flush() {
            original.flush()
            branch.flush()
        }

        @Throws(IOException::class)
        override fun close() {
            original.close()
            branch.close()
        }

        override fun isReady(): Boolean = original.isReady

        override fun setWriteListener(listener: WriteListener) {
            original.setWriteListener(listener)
        }
    }
}