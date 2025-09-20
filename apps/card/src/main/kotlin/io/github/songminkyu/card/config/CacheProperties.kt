package io.github.songminkyu.card.config

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
                var timeToLive: Long = 0,
                var maxIdleTime: Long = 0
            )
        }
    }
}