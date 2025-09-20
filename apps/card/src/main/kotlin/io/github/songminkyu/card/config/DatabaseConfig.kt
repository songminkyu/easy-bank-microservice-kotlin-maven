package io.github.songminkyu.card.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.h2.tools.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.sql.SQLException
import java.time.Clock
import java.time.Instant
import java.util.*

private val logger = KotlinLogging.logger {}

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = ["io.github.songminkyu.card.repository"])
@EnableJpaAuditing(
    dateTimeProviderRef = "dateTimeProvider",
    auditorAwareRef = "springSecurityAuditorAware"
)
@EnableTransactionManagement
class DatabaseConfig {

    @Bean
    fun clock(): Clock {
        return Clock.systemDefaultZone()
    }

    @Bean
    fun dateTimeProvider(clock: Clock): DateTimeProvider {
        return DateTimeProvider { Optional.of(Instant.now(clock)) }
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    @Throws(SQLException::class)
    fun h2TCPServer(environment: Environment): Server {
        val port = getValidPortForH2(environment)
        logger.debug { "H2 database is available on port $port" }
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", port)
    }

    private fun getValidPortForH2(env: Environment): String {
        var port = env.getProperty("server.port")!!.toInt()
        if (port < 10000) {
            port = 10000 + port
        } else {
            if (port < 63536) {
                port += 2000
            } else {
                port -= 2000
            }
        }
        return port.toString()
    }
}