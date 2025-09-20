package io.github.songminkyu.account.config

import io.github.songminkyu.account.logging.webclient.LoggingExchangeFilterFunction
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(WebClientProperties::class)
class WebClientConfig {

    companion object {
        private const val CUSTOMIZER_NAME = "oauth2AuthorizedClientExchangeFunction"
    }

    @Bean
    fun httpClient(webClientProperties: WebClientProperties): HttpClient {
        val config = webClientProperties.httpClient.config
        return HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.connectTimeout)
            .doOnConnected { conn ->
                conn
                    .addHandlerLast(ReadTimeoutHandler(config.readTimeout / 1000))
                    .addHandlerLast(WriteTimeoutHandler(config.writeTimeout / 1000))
            }
    }

    @Bean
    @ConditionalOnClass(WebClient::class)
    @LoadBalanced
    fun loadBalancedWebClientBuilder(
        httpClient: HttpClient,
        loggingExchangeFilterFunction: LoggingExchangeFilterFunction,
        oauth2AuthorizedClientExchangeFunction: ServletOAuth2AuthorizedClientExchangeFilterFunction,
        customizerProvider: ObjectProvider<WebClientCustomizer>
    ): WebClient.Builder {
        val builder = WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .filter(oauth2AuthorizedClientExchangeFunction)
            .filter(loggingExchangeFilterFunction)
        customizerProvider.orderedStream().forEach { customizer ->
            customizer.customize(builder)
        }
        return builder
    }

    @Bean
    fun authorizedClientManager(
        clientRegistrationRepository: ClientRegistrationRepository,
        authorizedClientService: OAuth2AuthorizedClientService
    ): OAuth2AuthorizedClientManager {
        val authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
            .clientCredentials()
            .build()

        val authorizedClientManager = AuthorizedClientServiceOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientService
        )
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)

        return authorizedClientManager
    }

    @Bean
    @ConditionalOnMissingBean(name = [CUSTOMIZER_NAME])
    @ConditionalOnClass(ExchangeFunction::class)
    fun oauth2AuthorizedClientExchangeFunction(
        authorizedClientManager: OAuth2AuthorizedClientManager,
        webClientProperties: WebClientProperties
    ): ServletOAuth2AuthorizedClientExchangeFilterFunction {
        val config = webClientProperties.oauth2
        val oauth2Client = ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oauth2Client.setDefaultClientRegistrationId(config.clientRegistrationId)
        return oauth2Client
    }
}