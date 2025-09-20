package io.github.songminkyu.account.exception.fauxpas

import java.util.function.Predicate

@FunctionalInterface
interface ThrowingPredicate<T, X : Throwable> : Predicate<T> {

    @Throws(Exception::class)
    fun tryTest(t: T): Boolean

    override fun test(t: T): Boolean {
        return try {
            tryTest(t)
        } catch (throwable: Throwable) {
            @Suppress("UNCHECKED_CAST")
            throw throwable as RuntimeException
        }
    }
}