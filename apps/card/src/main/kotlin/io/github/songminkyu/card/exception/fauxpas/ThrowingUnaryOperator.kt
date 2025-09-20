package io.github.songminkyu.card.exception.fauxpas

import java.util.function.UnaryOperator

interface ThrowingUnaryOperator<T, X : Throwable> : ThrowingFunction<T, T, X>, UnaryOperator<T>