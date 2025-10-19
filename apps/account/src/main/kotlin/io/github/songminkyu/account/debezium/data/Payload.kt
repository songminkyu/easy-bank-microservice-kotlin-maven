package io.github.songminkyu.account.debezium.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

@JvmRecord
data class Payload<T : Any?>(
    val before: T?,
    val after: T?,
    val source: MutableMap<String?, Any?>?,
    @field:JsonProperty("op") @param:JsonProperty("op") val operation: Operation?,
    @field:JsonProperty("ts_ms") @param:JsonProperty("ts_ms") val date: Instant?
) 