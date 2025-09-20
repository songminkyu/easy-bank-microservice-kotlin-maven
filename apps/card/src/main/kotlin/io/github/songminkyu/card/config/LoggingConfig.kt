package io.github.songminkyu.card.config

import ch.qos.logback.classic.LoggerContext
import com.github.loki4j.logback.AbstractLoki4jEncoder
import com.github.loki4j.logback.JavaHttpSender
import com.github.loki4j.logback.JsonEncoder
import com.github.loki4j.logback.JsonLayout
import com.github.loki4j.logback.Loki4jAppender
import graphql.GraphQL
import io.github.songminkyu.card.aspect.LoggingAspect
import io.github.songminkyu.card.constants.Constants
import io.github.songminkyu.card.logging.core.Sink
import io.github.songminkyu.card.logging.graphql.LoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.graphql.server.WebGraphQlInterceptor

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LoggingProperties::class)
class LoggingConfig(
    @Value("\${spring.application.name}") private val appName: String,
    loggingProperties: LoggingProperties
) {

    companion object {
        private const val LOKI_APPENDER_NAME = "LOKI"
        const val CUSTOMIZER_NAME = "loggingGraphQlInterceptor"
    }

    init {
        val context = LoggerFactory.getILoggerFactory() as LoggerContext

        if (loggingProperties.loki.enabled) {
            addLoki4jAppender(context, loggingProperties.loki)
        }
    }

    fun addLoki4jAppender(
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

        loki4jAppender.setHttp(httpSender)
        val format = getJsonEncoder(context)
        loki4jAppender.setFormat(format)
        loki4jAppender.start()
        context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(loki4jAppender)
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
    @ConditionalOnMissingBean(name = [CUSTOMIZER_NAME])
    @ConditionalOnClass(GraphQL::class)
    fun loggingGraphQlInterceptor(sink: Sink): WebGraphQlInterceptor {
        return LoggingInterceptor(sink)
    }
}