package io.github.songminkyu.card.security.websocket

import org.slf4j.LoggerFactory
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.graphql.server.WebSocketGraphQlInterceptor
import org.springframework.graphql.server.WebSocketGraphQlRequest
import org.springframework.graphql.server.WebSocketSessionInfo
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono
import java.util.*

@Component
class GraphQLWsAuthenticationInterceptor(
    private val authenticationProvider: AuthenticationProvider
) : WebSocketGraphQlInterceptor {

    companion object {
        private const val AUTHORIZATION_CONNECTION_INIT_PAYLOAD_KEY_NAME = "Authorization"
        private val AUTHENTICATION_SESSION_ATTRIBUTE_KEY =
            "${GraphQLWsAuthenticationInterceptor::class.java.canonicalName}.authentication"
        private val log = LoggerFactory.getLogger(GraphQLWsAuthenticationInterceptor::class.java)
    }

    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        if (request !is WebSocketGraphQlRequest) {
            return chain.next(request)
        }

        val optionalAuthentication = getAuthentication(request.sessionInfo)?.let { authenticate(it) }
        optionalAuthentication?.let { authentication ->
            SecurityContextHolder.getContext().authentication = authentication
        }
        return chain.next(request)
    }

    override fun handleCancelledSubscription(sessionInfo: WebSocketSessionInfo, subscriptionId: String): Mono<Void> {
        clearAuthentication(sessionInfo)
        return Mono.empty()
    }

    override fun handleConnectionClosed(
        sessionInfo: WebSocketSessionInfo,
        statusCode: Int,
        connectionInitPayload: Map<String, Any>
    ) {
        clearAuthentication(sessionInfo)
    }

    override fun handleConnectionInitialization(
        sessionInfo: WebSocketSessionInfo,
        connectionInitPayload: Map<String, Any>
    ): Mono<Any> {
        val accessToken = resolveToken(connectionInitPayload)
        if (StringUtils.hasText(accessToken)) {
            setAuthentication(sessionInfo, BearerTokenAuthenticationToken(accessToken))
        }
        return Mono.empty<Any>()
    }

    private fun resolveToken(connectionInitPayload: Map<String, Any>): String? {
        return resolveTokenFromPayload(connectionInitPayload)
    }

    private fun resolveTokenFromPayload(connectionInitPayload: Map<String, Any>): String? {
        val tokenFromPayload = connectionInitPayload[AUTHORIZATION_CONNECTION_INIT_PAYLOAD_KEY_NAME] as? String
        return if (tokenFromPayload != null &&
                   tokenFromPayload.startsWith(OAuth2AccessToken.TokenType.BEARER.value)) {
            tokenFromPayload.substring(OAuth2AccessToken.TokenType.BEARER.value.length)
        } else {
            null
        }
    }

    private fun getAuthentication(webSocketSessionInfo: WebSocketSessionInfo): BearerTokenAuthenticationToken? {
        return webSocketSessionInfo.attributes[AUTHENTICATION_SESSION_ATTRIBUTE_KEY] as? BearerTokenAuthenticationToken
    }

    private fun setAuthentication(
        webSocketSessionInfo: WebSocketSessionInfo,
        authentication: BearerTokenAuthenticationToken
    ) {
        webSocketSessionInfo.attributes[AUTHENTICATION_SESSION_ATTRIBUTE_KEY] = authentication
    }

    private fun clearAuthentication(webSocketSessionInfo: WebSocketSessionInfo) {
        webSocketSessionInfo.attributes.remove(AUTHENTICATION_SESSION_ATTRIBUTE_KEY)
    }

    private fun authenticate(authentication: Authentication): Authentication? {
        return try {
            authenticationProvider.authenticate(authentication)
        } catch (ex: AuthenticationException) {
            null
        }
    }
}