package io.github.songminkyu.card.logging.utils

import org.springframework.util.SerializationUtils
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

object ByteStreams {
    
    fun toByteArray(data: Any): ByteArray {
        return SerializationUtils.serialize(data)!!
    }
    
    @Throws(IOException::class)
    fun toByteArray(input: InputStream): ByteArray {
        val out = ByteArrayOutputStream()
        copy(input, out)
        return out.toByteArray()
    }
    
    @Throws(IOException::class)
    fun copy(from: InputStream, to: OutputStream) {
        Objects.requireNonNull(from)
        Objects.requireNonNull(to)
        val buf = ByteArray(4096)
        var bytesRead: Int
        
        while (from.read(buf).also { bytesRead = it } != -1) {
            to.write(buf, 0, bytesRead)
        }
    }
}