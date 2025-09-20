package io.github.songminkyu.card.exception.fauxpas

import java.util.function.Function

fun interface ThrowingFunction<T, R, X : Throwable> : Function<T, R> {

    @Throws(Throwable::class)
    fun tryApply(t: T): R

    override fun apply(t: T): R {
        return try {
            tryApply(t)
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }
}