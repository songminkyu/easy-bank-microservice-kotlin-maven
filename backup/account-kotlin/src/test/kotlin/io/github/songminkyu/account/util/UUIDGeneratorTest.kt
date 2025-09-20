package io.github.songminkyu.account.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class UUIDGeneratorTest {

    @Test
    fun testUUIDFromString() {
        val inputs = arrayOf("hello", "world", "test", "test")
        val expectedUUIDs = arrayOf(
            "5d41402a-bc4b-3a76-b971-9d911017c592",
            "7d793037-a076-3186-974b-0282f2f435e7",
            "098f6bcd-4621-3373-8ade-4e832627b4f6",
            "098f6bcd-4621-3373-8ade-4e832627b4f6"
        )

        for (i in inputs.indices) {
            val uuid: UUID = UUIDGenerator.generateUUIDFromString(inputs[i])
            assertEquals(expectedUUIDs[i], uuid.toString())
        }
    }
}