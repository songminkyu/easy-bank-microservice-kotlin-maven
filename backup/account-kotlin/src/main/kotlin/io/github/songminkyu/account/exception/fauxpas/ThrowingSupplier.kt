package io.github.songminkyu.account.exception.fauxpas

import java.util.function.Supplier

interface ThrowingSupplier<T, X : Throwable> : Supplier<T> {

    @Throws(Exception::class)
    fun tryGet(): T

    override fun get(): T {
        return try {
            tryGet()
        } catch (throwable: Throwable) {
            @Suppress("UNCHECKED_CAST")
            throw throwable as RuntimeException
        }
    }
}