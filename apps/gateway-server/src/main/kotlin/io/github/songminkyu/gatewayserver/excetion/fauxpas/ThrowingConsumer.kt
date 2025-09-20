package io.github.songminkyu.gatewayserver.excetion.fauxpas

import java.util.function.Consumer

@FunctionalInterface
interface ThrowingConsumer<T, X : Throwable> : Consumer<T> {

    @Throws(Exception::class)
    fun tryAccept(t: T)

    override fun accept(t: T) {
        try {
            tryAccept(t)
        } catch (throwable: Throwable) {
            @Suppress("UNCHECKED_CAST")
            throw throwable as RuntimeException
        }
    }
}