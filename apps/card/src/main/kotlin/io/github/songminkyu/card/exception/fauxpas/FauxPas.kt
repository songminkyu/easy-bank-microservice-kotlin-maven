package io.github.songminkyu.card.exception.fauxpas

@Suppress("unused")
object FauxPas {

    fun <X : Throwable> throwingRunnable(
        runnable: ThrowingRunnable<X>
    ): ThrowingRunnable<X> {
        return runnable
    }

    fun <T, X : Throwable> throwingConsumer(
        consumer: ThrowingConsumer<T, X>
    ): ThrowingConsumer<T, X> {
        return consumer
    }

    fun <T, R, X : Throwable> throwingFunction(
        function: ThrowingFunction<T, R, X>
    ): ThrowingFunction<T, R, X> {
        return function
    }

    fun <T, X : Throwable> throwingSupplier(
        supplier: ThrowingSupplier<T, X>
    ): ThrowingSupplier<T, X> {
        return supplier
    }

    fun <T, X : Throwable> throwingUnaryOperator(
        operator: ThrowingUnaryOperator<T, X>
    ): ThrowingUnaryOperator<T, X> {
        return operator
    }

    fun <T, X : Throwable> throwingPredicate(
        predicate: ThrowingPredicate<T, X>
    ): ThrowingPredicate<T, X> {
        return predicate
    }
}