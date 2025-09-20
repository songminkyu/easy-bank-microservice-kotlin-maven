package io.github.songminkyu.account.debezium.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class Payload<T : Any>(
    val before: T?,
    val after: T?,
    val source: Map<String, Any?>,
    @JsonProperty("op") val operation: Operation,
    @JsonProperty("ts_ms") val date: Instant
)