package io.github.songminkyu.account.exception.fauxpas

import java.util.function.Function

@FunctionalInterface
interface ThrowingFunction<T, R, X : Throwable> : Function<T, R> {

    @Throws(Exception::class)
    fun tryApply(t: T): R

    override fun apply(t: T): R {
        return try {
            tryApply(t)
        } catch (throwable: Throwable) {
            @Suppress("UNCHECKED_CAST")
            throw throwable as RuntimeException
        }
    }
}