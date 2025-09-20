package io.github.songminkyu.account.util

import java.util.UUID

object UUIDGenerator {
    fun generateUUIDFromString(input: String): UUID {
        return UUID.nameUUIDFromBytes(input.toByteArray())
    }
}
