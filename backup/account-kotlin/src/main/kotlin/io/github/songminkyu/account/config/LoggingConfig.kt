package io.github.songminkyu.account.config

import ch.qos.logback.classic.LoggerContext
import com.github.loki4j.logback.AbstractLoki4jEncoder
import com.github.loki4j.logback.JavaHttpSender
import com.github.loki4j.logback.JsonEncoder
import com.github.loki4j.logback.JsonLayout
import com.github.loki4j.logback.Loki4jAppender
import feign.Logger
import io.github.songminkyu.account.aspect.LoggingAspect
import io.github.songminkyu.account.constants.Constants
import io.github.songminkyu.account.logging.core.Sink
import io.github.songminkyu.account.logging.feign.DefaultFeignLogger
import io.github.songminkyu.account.logging.restclient.LoggingInterceptor
import io.github.songminkyu.account.logging.servlet.LoggingFilter
import io.github.songminkyu.account.logging.webclient.LoggingExchangeFilterFunction
import jakarta.servlet.DispatcherType
import jakarta.servlet.Filter
import org.slf4j.Logger.ROOT_LOGGER_NAME
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered
import org.springframework.core.env.Environment
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.web.reactive.function.client.ExchangeFunction

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LoggingProperties::class)
@EnableAspectJAutoProxy
class LoggingConfig(
    @Value("\${spring.application.name}") private val appName: String,
    loggingProperties: LoggingProperties
) {

    companion object {
        private const val LOKI_APPENDER_NAME = "LOKI"
        private const val CUSTOMIZER_NAME = "loggingClientExchangeFunction"
        private const val INTERCEPTOR_NAME = "loggingInterceptor"
        private const val FILTER_NAME = "logging.filter"

        fun newFilter(filter: Filter, filterName: String, order: Int): FilterRegistrationBean<*> {
            return FilterRegistrationBean(filter).apply {
                setName(filterName)
                setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC)
                setOrder(order)
            }
        }
    }

    init {
        val context = LoggerFactory.getILoggerFactory() as LoggerContext
        if (loggingProperties.loki.enabled) {
            addLoki4jAppender(context, loggingProperties.loki)
        }
    }

    private fun addLoki4jAppender(
        context: LoggerContext,
        lokiProperties: LoggingProperties.Loki
    ) {
        val loki4jAppender = Loki4jAppender().apply {
            setContext(context)
            name = LOKI_APPENDER_NAME
        }

        val httpSender = JavaHttpSender().apply {
            url = lokiProperties.url
        }
        loki4jAppender.setHttp(httpSender);
        val encoder = getJsonEncoder(context)
        loki4jAppender.setFormat(encoder);
        loki4jAppender.start()

        context.getLogger(ROOT_LOGGER_NAME).addAppender(loki4jAppender)
    }
    private fun getJsonEncoder(context: LoggerContext): JsonEncoder {
        val encoder = JsonEncoder().apply {
            setContext(context)
        }

        val label = AbstractLoki4jEncoder.LabelCfg().apply {
            setReadMarkers(true)
            setPattern("app=$appName,host=\${HOSTNAME},level=%level")
        }
        encoder.setLabel(label)
        encoder.setSortByTime(true)

        val jsonLayout = JsonLayout()
        encoder.setMessage(jsonLayout);
        encoder.start()

        return encoder
    }
    @Bean
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    fun loggingAspect(env: Environment): LoggingAspect {
        return LoggingAspect(env)
    }

    @Bean
    @ConditionalOnMissingBean(name = [FILTER_NAME])
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(jakarta.servlet.Servlet::class)
    fun loggingFilter(sink: Sink): FilterRegistrationBean<*> {
        val filter = LoggingFilter(sink)
        return newFilter(filter, FILTER_NAME, Ordered.LOWEST_PRECEDENCE)
    }

    @Bean
    @ConditionalOnMissingBean(Logger::class)
    @ConditionalOnClass(Logger::class)
    fun defaultFeignLogger(sink: Sink): Logger {
        return DefaultFeignLogger(sink)
    }

    @Bean
    @ConditionalOnMissingBean(name = [CUSTOMIZER_NAME])
    @ConditionalOnClass(ExchangeFunction::class)
    fun loggingClientExchangeFunction(sink: Sink): LoggingExchangeFilterFunction {
        return LoggingExchangeFilterFunction(sink)
    }

    @Bean
    @ConditionalOnMissingBean(name = [INTERCEPTOR_NAME])
    @ConditionalOnClass(ClientHttpRequestExecution::class)
    fun loggingInterceptor(sink: Sink): LoggingInterceptor {
        return LoggingInterceptor(sink)
    }
}