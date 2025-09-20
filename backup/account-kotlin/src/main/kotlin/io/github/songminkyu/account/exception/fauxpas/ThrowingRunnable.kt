package io.github.songminkyu.account.exception.fauxpas

interface ThrowingRunnable<T : Throwable> : Runnable {

    @Throws(Exception::class)
    fun tryRun()

    override fun run() {
        try {
            tryRun()
        } catch (throwable: Throwable) {
            @Suppress("UNCHECKED_CAST")
            throw throwable as RuntimeException
        }
    }
}