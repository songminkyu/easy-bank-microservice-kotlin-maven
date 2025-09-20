package io.github.songminkyu.card.exception.fauxpas

import java.io.IOException
import java.util.function.Supplier

fun interface ThrowingSupplier<T, X : Throwable> : Supplier<T> {

    @Throws(Throwable::class, IOException::class)
    fun tryGet(): T

    override fun get(): T {
        return try {
            tryGet()
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }
}