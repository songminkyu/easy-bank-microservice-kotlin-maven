package io.github.songminkyu.account.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import io.opentelemetry.semconv.SemanticAttributes.EXCEPTION_MESSAGE
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.core.task.AsyncTaskExecutor
import java.util.concurrent.Callable
import java.util.concurrent.Future

class ExceptionHandlingAsyncTaskExecutor(
    private val executor: AsyncTaskExecutor
) : AsyncTaskExecutor, InitializingBean, DisposableBean {

    private val logger = KotlinLogging.logger {}

    /**
     * <p>Constructor for ExceptionHandlingAsyncTaskExecutor.</p>
     *
     * @param executor a [AsyncTaskExecutor] object.
     */

    override fun execute(task: Runnable) {
        executor.execute(createWrappedRunnable(task))
    }

    @Deprecated("since 7.8.0", replaceWith = ReplaceWith("execute(task)"))
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

    /**
     * <p>handle.</p>
     *
     * @param e a [Exception] object.
     */
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