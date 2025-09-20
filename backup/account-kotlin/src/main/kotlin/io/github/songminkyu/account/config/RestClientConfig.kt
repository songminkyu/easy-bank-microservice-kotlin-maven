package io.github.songminkyu.account.config

import io.github.songminkyu.account.logging.restclient.LoggingInterceptor
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.web.client.RestClientCustomizer
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration(proxyBeanMethods = false)
class RestClientConfig {

    @Bean
    @ConditionalOnClass(RestClient::class)
    @LoadBalanced
    fun loadBalancedWebClientBuilder(
        loggingInterceptor: LoggingInterceptor,
        customizerProvider: ObjectProvider<RestClientCustomizer>
    ): RestClient.Builder {
        val builder = RestClient.builder()
            .requestInterceptor(loggingInterceptor)
        customizerProvider.orderedStream().forEach { customizer ->
            customizer.customize(builder)
        }
        return builder
    }
}