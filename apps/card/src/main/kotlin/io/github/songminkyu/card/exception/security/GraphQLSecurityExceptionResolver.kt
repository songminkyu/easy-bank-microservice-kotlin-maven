package io.github.songminkyu.card.exception.security

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import io.github.songminkyu.card.exception.AbstractGraphQLExceptionHandler
import org.springframework.graphql.execution.ErrorType
import org.springframework.graphql.execution.SubscriptionExceptionResolver
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationTrustResolver
import org.springframework.security.authentication.AuthenticationTrustResolverImpl
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GraphQLSecurityExceptionResolver : AbstractGraphQLExceptionHandler(), SubscriptionExceptionResolver {

    private val trustResolver: AuthenticationTrustResolver = AuthenticationTrustResolverImpl()

    private fun resolveUnauthorized(): GraphQLError {
        return GraphqlErrorBuilder.newError()
            .errorType(ErrorType.UNAUTHORIZED)
            .message(HttpStatus.UNAUTHORIZED.reasonPhrase)
            .build()
    }

    private fun resolveAccessDenied(securityContext: SecurityContext): GraphQLError {
        return if (trustResolver.isAnonymous(securityContext.authentication)) {
            resolveUnauthorized()
        } else {
            GraphqlErrorBuilder.newError()
                .errorType(ErrorType.FORBIDDEN)
                .message(HttpStatus.FORBIDDEN.reasonPhrase)
                .build()
        }
    }

    override fun resolveException(exception: Throwable): Mono<List<GraphQLError>> {
        return when (exception) {
            is AuthenticationException -> {
                val error = resolveUnauthorized()
                Mono.just(listOf(error))
            }
            is AccessDeniedException -> {
                ReactiveSecurityContextHolder.getContext()
                    .map { context -> listOf(resolveAccessDenied(context)) }
                    .switchIfEmpty(Mono.fromCallable { listOf(resolveUnauthorized()) })
            }
            else -> Mono.empty()
        }
    }

    fun resolveException(exception: Throwable, env: DataFetchingEnvironment): GraphQLError {
        return when (exception) {
            is AuthenticationException -> resolveUnauthorized()
            is AccessDeniedException -> {
                SecurityContextHolder.getContext()?.let { resolveAccessDenied(it) } ?: resolveUnauthorized()
            }
            else -> handleExceptionInternal(exception, ErrorType.INTERNAL_ERROR, env)
        }
    }
}