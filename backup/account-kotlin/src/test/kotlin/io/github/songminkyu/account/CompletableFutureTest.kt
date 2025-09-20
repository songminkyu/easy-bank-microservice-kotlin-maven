package io.github.songminkyu.account

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

class CompletableFutureTest {

    @Test
    @Throws(ExecutionException::class, InterruptedException::class)
    fun testsCompletableFuture() {
        val future1 = CompletableFuture.supplyAsync {
            try {
                // Simulate a long-running operation
                Thread.sleep(2000) // 2 seconds
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            "Result of Future 1"
        }

        val future2 = CompletableFuture.supplyAsync {
            try {
                // Simulate another long-running operation
                Thread.sleep(1500) // 1.5 seconds
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            "Result of Future 2"
        }

        val combinedFuture = CompletableFuture.allOf(future1, future2)

        combinedFuture.join()

        // Block and wait for all futures to complete
        combinedFuture.join()
        val result1 = future1.get()
        val result2 = future2.get()
        assertNotNull(result1)
        assertNotNull(result2)
    }
}