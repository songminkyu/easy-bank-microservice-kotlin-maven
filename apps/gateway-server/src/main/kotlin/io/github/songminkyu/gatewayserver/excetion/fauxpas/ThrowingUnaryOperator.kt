package io.github.songminkyu.gatewayserver.excetion.fauxpas

import java.util.function.UnaryOperator

interface ThrowingUnaryOperator<T, X : Throwable> : ThrowingFunction<T, T, X>, UnaryOperator<T>