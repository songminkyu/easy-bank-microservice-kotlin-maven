package io.github.songminkyu.account.util

import java.util.*

object UUIDGenerator {
    @JvmStatic
    fun generateUUIDFromString(input: String): UUID {
        return UUID.nameUUIDFromBytes(input.toByteArray())
    }
}