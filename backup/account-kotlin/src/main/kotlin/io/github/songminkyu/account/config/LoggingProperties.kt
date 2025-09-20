package io.github.songminkyu.account.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.logging")
data class LoggingProperties(
    var loki: Loki = Loki()
) {

    data class Loki(
        var enabled: Boolean = LoggingDefaults.Loki.ENABLED,
        var url: String = LoggingDefaults.Loki.URL
    )
}