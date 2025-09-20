package io.github.songminkyu.account.debezium.data

import com.fasterxml.jackson.annotation.JsonValue

enum class Operation(
    @get:JsonValue val code: String
) {
    CREATE("c"),
    UPDATE("u"),
    DELETE("r")
}