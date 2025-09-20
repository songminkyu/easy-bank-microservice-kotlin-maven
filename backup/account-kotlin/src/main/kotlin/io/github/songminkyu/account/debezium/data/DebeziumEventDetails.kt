package io.github.songminkyu.account.debezium.data

data class DebeziumEventDetails<T : Any>(
    val schema: Map<String, Any?>,
    val payload: Payload<T>
)