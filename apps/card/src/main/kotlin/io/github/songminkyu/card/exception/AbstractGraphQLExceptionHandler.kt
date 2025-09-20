package io.github.songminkyu.card.exception

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import io.github.songminkyu.card.exception.ErrorConstants.ERR_INTERNAL_SERVER
import org.slf4j.LoggerFactory
import org.springframework.graphql.execution.ErrorType

abstract class AbstractGraphQLExceptionHandler {

    companion object {
        private const val ERROR_LOG_MSG = "An exception occurred, which will cause response"
        private val log = LoggerFactory.getLogger(AbstractGraphQLExceptionHandler::class.java)
    }

    protected fun handleValidationException(env: DataFetchingEnvironment, ex: Exception): GraphQLError {
        return handleExceptionInternal(ex, ErrorType.BAD_REQUEST, env)
    }

    protected fun handleExceptionInternal(
        ex: Throwable,
        errorType: ErrorType,
        env: DataFetchingEnvironment
    ): GraphQLError {
        return when (errorType) {
            ErrorType.INTERNAL_ERROR -> {
                log.error(ERROR_LOG_MSG, ex)
                createGraphQLError(env, ERR_INTERNAL_SERVER, errorType)
            }
            ErrorType.BAD_REQUEST -> {
                log.warn(ERROR_LOG_MSG, ex)
                createGraphQLError(env, ex.message ?: "", errorType)
            }
            else -> {
                log.debug(ERROR_LOG_MSG, ex)
                createGraphQLError(env, ex.message ?: "", errorType)
            }
        }
    }

    protected fun createGraphQLError(
        env: DataFetchingEnvironment,
        errorMessage: String,
        errorType: ErrorType
    ): GraphQLError {
        return GraphqlErrorBuilder.newError(env)
            .message(errorMessage)
            .errorType(errorType)
            .build()
    }
}