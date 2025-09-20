package io.github.songminkyu.account.exception

import io.github.songminkyu.account.dto.Violation
import io.github.songminkyu.account.exception.ErrorConstants.ERR_INTERNAL_SERVER
import io.github.songminkyu.account.exception.ErrorConstants.ERR_VALIDATION
import io.github.songminkyu.account.exception.ErrorConstants.PROBLEM_VIOLATION_KEY
import jakarta.validation.ConstraintViolationException
import org.springframework.graphql.ResponseError
import org.springframework.graphql.client.FieldAccessException
import org.springframework.graphql.client.GraphQlClientException
import org.springframework.graphql.execution.ErrorType
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.util.CollectionUtils
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.concurrent.CompletionException
import java.util.stream.Stream

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleHttpMediaTypeNotAcceptable(
        ex: HttpMediaTypeNotAcceptableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, ex.message)
        return handleExceptionInternal(
            ex, problem, headers,
            HttpStatusCode.valueOf(problem.status), request
        )
    }


    override fun handleHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.message)
        headers.accept = ex.supportedMediaTypes
        return handleExceptionInternal(
            ex, problem, headers,
            HttpStatusCode.valueOf(problem.status), request
        )
    }

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val methods = ex.supportedMethods?.toSet()
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, ex.message)

        if (CollectionUtils.isEmpty(methods)) {
            return handleExceptionInternal(
                ex, problem, null,
                HttpStatusCode.valueOf(problem.status), request
            )
        }

        ex.supportedHttpMethods?.let { headers.allow = it }
        return handleExceptionInternal(
            ex, problem, headers,
            HttpStatusCode.valueOf(problem.status), request
        )
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ERR_VALIDATION)
        val fieldErrors = ex.fieldErrors.map { Violation(it) }
        val globalErrors = ex.globalErrors.map { Violation(it) }
        val violations = fieldErrors + globalErrors
        problem.setProperty(PROBLEM_VIOLATION_KEY, violations)
        return handleExceptionInternal(
            ex, problem, headers,
            HttpStatusCode.valueOf(problem.status), request
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ERR_VALIDATION)
        val violations = ex.constraintViolations.map { Violation(it) }
        problem.setProperty(PROBLEM_VIOLATION_KEY, violations)

        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), request
        )!!
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthentication(
        ex: AuthenticationException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.message)
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )!!
    }


    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(
        ex: AccessDeniedException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.message)
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )!!
    }


    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFound(
        ex: EntityNotFoundException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )!!
    }

    @ExceptionHandler(CompletionException::class)
    fun handleCompletionException(
        ex: CompletionException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        return when (val cause = ex.cause) {
            is CustomerAlreadyExistsException -> handleCustomerAlreadyExistsException(cause, webRequest)
            else -> handleAll(ex, webRequest)
        }!!
    }

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException::class)
    fun handleEntityNotFound(
        ex: jakarta.persistence.EntityNotFoundException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )!!
    }


    @ExceptionHandler(CustomerAlreadyExistsException::class)
    fun handleCustomerAlreadyExistsException(
        ex: CustomerAlreadyExistsException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.message)
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )!!
    }


    @ExceptionHandler(FieldAccessException::class)
    fun handleFieldAccessException(
        ex: FieldAccessException,
        webRequest: WebRequest
    ): ResponseEntity<out Any?>? {
        val responseErrorOptional = ex.response.errors
            ?.takeIf { !CollectionUtils.isEmpty(it) }
            ?.firstOrNull()

        return (if (responseErrorOptional != null) {
            handleGraphQlClientException(ex, responseErrorOptional, webRequest)
        } else {
            val problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)
            handleExceptionInternal(
                ex, problem, null,
                HttpStatusCode.valueOf(problem.status), webRequest
            )
        })
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(
        ex: Exception,
        webRequest: WebRequest
    ): ResponseEntity<Any>? {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders?,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        when {
            statusCode.is5xxServerError -> {
                logger.error(ex.message, ex)
                val problem = ProblemDetail.forStatusAndDetail(statusCode,
                    ERR_INTERNAL_SERVER
                )
                return super.handleExceptionInternal(ex, problem, headers, statusCode, request)
            }
            statusCode.is4xxClientError -> {
                logger.warn(ex.message, ex)
            }
            else -> {
                logger.debug(ex.message, ex)
            }
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request)
    }
    protected fun handleGraphQlClientException(
        ex: GraphQlClientException,
        responseError: ResponseError,
        webRequest: WebRequest
    ): ResponseEntity<Any>? {
        val extensions = responseError.extensions
        val classification = extensions["classification"]
        val problem = ErrorType.values()
            .firstOrNull { it.name == classification }
            ?.let { errorType ->
                when (errorType) {
                    ErrorType.BAD_REQUEST -> ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST, ex.message
                    )
                    ErrorType.NOT_FOUND -> ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND, ex.message
                    )
                    else -> ProblemDetail.forStatusAndDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR, ERR_INTERNAL_SERVER
                    )
                }
            } ?: ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, ERR_INTERNAL_SERVER
            )
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )
    }
}