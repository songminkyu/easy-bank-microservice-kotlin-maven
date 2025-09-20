package io.github.songminkyu.card.config

import io.github.songminkyu.card.exception.ExceptionHandlingAsyncTaskExecutor
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

@Configuration(proxyBeanMethods = false)
@EnableAsync
@EnableScheduling
class AsyncConfig(
    private val taskExecutionProperties: TaskExecutionProperties
) : AsyncConfigurer {

    @Bean(name = ["taskExecutor"])
    override fun getAsyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor().apply {
            corePoolSize = taskExecutionProperties.pool.coreSize
            maxPoolSize = taskExecutionProperties.pool.maxSize
            queueCapacity = taskExecutionProperties.pool.queueCapacity
            setThreadNamePrefix(taskExecutionProperties.threadNamePrefix)
            setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
            initialize()
        }
        return ExceptionHandlingAsyncTaskExecutor(executor)
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
        return SimpleAsyncUncaughtExceptionHandler()
    }
}