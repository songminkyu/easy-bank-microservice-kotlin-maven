package io.github.songminkyu.loan.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.core.task.AsyncTaskExecutor
import java.util.concurrent.Callable
import java.util.concurrent.Future

private val logger = KotlinLogging.logger {}

class ExceptionHandlingAsyncTaskExecutor(
    private val executor: AsyncTaskExecutor
) : AsyncTaskExecutor, InitializingBean, DisposableBean {

    companion object {
        const val EXCEPTION_MESSAGE = "Caught async exception"
    }

    override fun execute(task: Runnable) {
        executor.execute(createWrappedRunnable(task))
    }

    @Deprecated("Deprecated in Java", ReplaceWith("execute(task)"))
    override fun execute(task: Runnable, startTimeout: Long) {
        executor.execute(createWrappedRunnable(task), startTimeout)
    }

    private fun <T> createCallable(task: Callable<T>): Callable<T> {
        return Callable {
            try {
                task.call()
            } catch (e: Exception) {
                handle(e)
                throw e
            }
        }
    }

    private fun createWrappedRunnable(task: Runnable): Runnable {
        return Runnable {
            try {
                task.run()
            } catch (e: Exception) {
                handle(e)
            }
        }
    }

    protected fun handle(e: Exception) {
        logger.error(e) { EXCEPTION_MESSAGE }
    }

    override fun submit(task: Runnable): Future<*> {
        return executor.submit(createWrappedRunnable(task))
    }

    override fun <T> submit(task: Callable<T>): Future<T> {
        return executor.submit(createCallable(task))
    }

    @Throws(Exception::class)
    override fun destroy() {
        if (executor is DisposableBean) {
            executor.destroy()
        }
    }

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        if (executor is InitializingBean) {
            executor.afterPropertiesSet()
        }
    }
}