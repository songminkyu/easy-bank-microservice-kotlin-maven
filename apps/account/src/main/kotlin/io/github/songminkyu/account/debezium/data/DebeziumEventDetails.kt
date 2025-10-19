package io.github.songminkyu.account.debezium.data

@JvmRecord
data class DebeziumEventDetails<T : Any?>(
    val schema: MutableMap<String?, Any?>?,
    val payload: Payload<T?>?
) 