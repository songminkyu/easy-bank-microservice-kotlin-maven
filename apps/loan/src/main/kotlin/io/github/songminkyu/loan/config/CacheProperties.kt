package io.github.songminkyu.loan.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.cache")
data class CacheProperties(
    var redisson: Redisson = Redisson()
) {

    data class Redisson(
        var regions: Map<String, Region> = emptyMap()
    ) {

        data class Region(
            var expiration: Expiration = Expiration()
        ) {

            data class Expiration(
                var maxEntries: Int = 0,
                var timeToLive: Long = 0L,
                var maxIdleTime: Long = 0L
            )
        }
    }
}