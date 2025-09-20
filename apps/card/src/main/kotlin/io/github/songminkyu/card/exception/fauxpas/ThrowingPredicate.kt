package io.github.songminkyu.card.exception.fauxpas

import java.util.function.Predicate

fun interface ThrowingPredicate<T, X : Throwable> : Predicate<T> {

    @Throws(Throwable::class)
    fun tryTest(t: T): Boolean

    override fun test(t: T): Boolean {
        return try {
            tryTest(t)
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }
}