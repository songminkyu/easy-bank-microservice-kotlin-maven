package io.github.songminkyu.card.debezium.data

data class DebeziumEventDetails<T>(
    val schema: Map<String, Any>,
    val payload: Payload<T>
)