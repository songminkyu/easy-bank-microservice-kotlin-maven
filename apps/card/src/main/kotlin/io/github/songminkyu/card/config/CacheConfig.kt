package io.github.songminkyu.card.config

import io.github.songminkyu.card.repository.CardRepository.Companion.CARD_BY_MOBILE_NUMBER_CACHE
import org.hibernate.boot.registry.StandardServiceRegistry
import org.hibernate.cfg.AvailableSettings
import org.redisson.api.RedissonClient
import org.redisson.hibernate.RedissonRegionFactory
import org.redisson.spring.cache.RedissonSpringCacheManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedissonClient::class)
@EnableCaching
@EnableConfigurationProperties(CacheProperties::class)
class CacheConfig(
    private val cacheProperties: CacheProperties
) {

    companion object {
        private const val ENTRY_REGION_NAME = "entry"
    }

    @Bean
    @ConditionalOnProperty("spring.jpa.properties.hibernate.cache.use_second_level_cache")
    fun hibernatePropertiesCustomizer(redissonClient: RedissonClient): HibernatePropertiesCustomizer {
        return HibernatePropertiesCustomizer { hibernateProperties ->
            hibernateProperties[AvailableSettings.CACHE_REGION_FACTORY] = object : RedissonRegionFactory() {
                override fun createRedissonClient(registry: StandardServiceRegistry?, properties: Map<*, *>?): RedissonClient {
                    return redissonClient
                }
            }
        }
    }

    @Bean
    fun cacheManager(redissonClient: RedissonClient): CacheManager {
        val config = mutableMapOf<String, org.redisson.spring.cache.CacheConfig>()
        createCache(config, CARD_BY_MOBILE_NUMBER_CACHE)
        return RedissonSpringCacheManager(redissonClient, config)
    }

    private fun createCache(
        cacheConfigMap: MutableMap<String, org.redisson.spring.cache.CacheConfig>,
        cacheName: String
    ) {
        val regions = cacheProperties.redisson.regions
        val region = regions[cacheName] ?: regions[ENTRY_REGION_NAME]!!
        val expiration = region.expiration
        val cacheConfig = org.redisson.spring.cache.CacheConfig(
            expiration.timeToLive, expiration.maxIdleTime
        )
        cacheConfig.maxSize = expiration.maxEntries
        cacheConfigMap[cacheName] = cacheConfig
    }
}