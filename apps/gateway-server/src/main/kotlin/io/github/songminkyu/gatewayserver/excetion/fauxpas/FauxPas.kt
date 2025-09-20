package io.github.songminkyu.gatewayserver.excetion.fauxpas

object FauxPas {

    fun <X : Throwable> throwingRunnable(
        runnable: ThrowingRunnable<X>
    ): ThrowingRunnable<X> = runnable

    fun <T, X : Throwable> throwingConsumer(
        consumer: ThrowingConsumer<T, X>
    ): ThrowingConsumer<T, X> = consumer

    fun <T, R, X : Throwable> throwingFunction(
        function: ThrowingFunction<T, R, X>
    ): ThrowingFunction<T, R, X> = function

    fun <T, X : Throwable> throwingSupplier(
        supplier: ThrowingSupplier<T, X>
    ): ThrowingSupplier<T, X> = supplier

    fun <T, X : Throwable> throwingUnaryOperator(
        operator: ThrowingUnaryOperator<T, X>
    ): ThrowingUnaryOperator<T, X> = operator

    fun <T, X : Throwable> throwingPredicate(
        predicate: ThrowingPredicate<T, X>
    ): ThrowingPredicate<T, X> = predicate
}