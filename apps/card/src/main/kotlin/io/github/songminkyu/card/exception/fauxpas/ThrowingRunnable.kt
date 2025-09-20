package io.github.songminkyu.card.exception.fauxpas

fun interface ThrowingRunnable<T : Throwable> : Runnable {

    @Throws(Throwable::class)
    fun tryRun()

    override fun run() {
        try {
            tryRun()
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }
}