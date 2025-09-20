
package io.github.songminkyu.loan.config

import ch.qos.logback.classic.LoggerContext
import com.github.loki4j.logback.AbstractLoki4jEncoder
import com.github.loki4j.logback.JavaHttpSender
import com.github.loki4j.logback.JsonEncoder
import com.github.loki4j.logback.JsonLayout
import com.github.loki4j.logback.Loki4jAppender
import io.github.songminkyu.loan.aspect.LoggingAspect
import io.github.songminkyu.loan.constants.Constants
import org.slf4j.Logger.ROOT_LOGGER_NAME
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LoggingProperties::class)
class LoggingConfig(
    @Value("\${spring.application.name}") private val appName: String,
    loggingProperties: LoggingProperties
) {

    companion object {
        private const val LOKI_APPENDER_NAME = "LOKI"
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
    fun loggingAspect(env: Environment): LoggingAspect = LoggingAspect(env)
}