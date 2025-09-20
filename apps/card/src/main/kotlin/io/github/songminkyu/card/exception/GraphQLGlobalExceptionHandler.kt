package io.github.songminkyu.card.exception

import graphql.GraphQLError
import graphql.schema.DataFetchingEnvironment
import io.github.songminkyu.card.exception.security.GraphQLSecurityExceptionResolver
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler
import org.springframework.graphql.execution.ErrorType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class GraphQLGlobalExceptionHandler(
    private val securityExceptionResolver: GraphQLSecurityExceptionResolver
) : AbstractGraphQLExceptionHandler() {

    @GraphQlExceptionHandler
    fun handleEntityNotFound(
        ex: EntityNotFoundException,
        env: DataFetchingEnvironment
    ): GraphQLError {
        return handleExceptionInternal(ex, ErrorType.NOT_FOUND, env)
    }

    @GraphQlExceptionHandler
    fun handleEntityNotFound(
        ex: jakarta.persistence.EntityNotFoundException,
        env: DataFetchingEnvironment
    ): GraphQLError {
        return handleExceptionInternal(ex, ErrorType.NOT_FOUND, env)
    }

    @GraphQlExceptionHandler
    fun handleCardAlreadyExistsException(
        ex: CardAlreadyExistsException,
        env: DataFetchingEnvironment
    ): GraphQLError {
        return handleValidationException(env, ex)
    }

    @GraphQlExceptionHandler
    fun handleAuthenticationException(
        ex: AuthenticationException,
        env: DataFetchingEnvironment
    ): GraphQLError {
        return securityExceptionResolver.resolveException(ex, env)
    }

    @GraphQlExceptionHandler
    fun handleAll(
        ex: Exception,
        env: DataFetchingEnvironment
    ): GraphQLError {
        return handleExceptionInternal(ex, ErrorType.INTERNAL_ERROR, env)
    }
}