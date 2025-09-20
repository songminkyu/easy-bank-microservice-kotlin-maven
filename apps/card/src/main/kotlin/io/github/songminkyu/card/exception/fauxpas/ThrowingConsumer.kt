package io.github.songminkyu.card.exception.fauxpas

import java.util.function.Consumer

fun interface ThrowingConsumer<T, X : Throwable> : Consumer<T> {

    @Throws(Throwable::class)
    fun tryAccept(t: T)

    override fun accept(t: T) {
        try {
            tryAccept(t)
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }
}