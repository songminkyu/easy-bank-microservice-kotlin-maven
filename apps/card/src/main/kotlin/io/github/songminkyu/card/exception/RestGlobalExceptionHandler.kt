package io.github.songminkyu.card.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.songminkyu.card.dto.Violation
import io.github.songminkyu.card.exception.ErrorConstants.ERR_INTERNAL_SERVER
import io.github.songminkyu.card.exception.ErrorConstants.ERR_VALIDATION
import io.github.songminkyu.card.exception.ErrorConstants.PROBLEM_VIOLATION_KEY
import jakarta.validation.ConstraintViolationException
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
import java.util.Objects.requireNonNull
import java.util.stream.Stream

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class RestGlobalExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleHttpMediaTypeNotAcceptable(
        ex: HttpMediaTypeNotAcceptableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_ACCEPTABLE, ex.message
        )
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
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.message
        )
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
        val methods = ex.supportedMethods?.let { setOf(*it) } ?: emptySet()
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.METHOD_NOT_ALLOWED, ex.message
        )
        if (CollectionUtils.isEmpty(methods)) {
            return handleExceptionInternal(
                ex, problem, null,
                HttpStatusCode.valueOf(problem.status), request
            )
        }
        headers.allow = requireNonNull(ex.supportedHttpMethods)
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
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, ERR_VALIDATION
        )
        val fieldErrors = ex.fieldErrors.stream().map { Violation(it) }
        val globalErrors = ex.globalErrors.stream().map { Violation(it) }
        val violations = Stream.concat(fieldErrors, globalErrors).toList()
        problem.setProperty(PROBLEM_VIOLATION_KEY, violations)
        return handleExceptionInternal(
            ex, problem, headers,
            HttpStatusCode.valueOf(problem.status), request
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, ERR_VALIDATION
        )
        val violations = ex.constraintViolations.stream().map { Violation(it) }.toList()
        problem.setProperty(PROBLEM_VIOLATION_KEY, violations)

        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), request
        )
    }

    @ExceptionHandler(AuthenticationException::class)
    protected fun handleAuthentication(
        ex: AuthenticationException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED, ex.message
        )
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )
    }

    @ExceptionHandler(AccessDeniedException::class)
    protected fun handleAccessDenied(
        ex: AccessDeniedException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.FORBIDDEN, ex.message
        )
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFound(
        ex: EntityNotFoundException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, ex.message
        )
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )
    }

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException::class)
    protected fun handleEntityNotFound(
        ex: jakarta.persistence.EntityNotFoundException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, ex.message
        )
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )
    }

    @ExceptionHandler(CardAlreadyExistsException::class)
    fun handleCardAlreadyExistsException(
        ex: CardAlreadyExistsException,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, ex.message
        )
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(
        ex: Exception,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, ex.message
        )
        return handleExceptionInternal(
            ex, problem, null,
            HttpStatusCode.valueOf(problem.status), webRequest
        )
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders?,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        return when {
            status.is5xxServerError -> {
                // kotlin-logging의 올바른 메서드 시그니처: 예외를 두 번째 파라미터로
                logger.error("An exception occurred, which will cause a $status response", ex)
                val problem = ProblemDetail.forStatusAndDetail(status, ERR_INTERNAL_SERVER)
                super.handleExceptionInternal(ex, problem, headers, status, request)
            }
            status.is4xxClientError -> {
                logger.warn("An exception occurred, which will cause a $status response",ex)
                super.handleExceptionInternal(ex, body, headers, status, request)
            }
            else -> {
                logger.debug("An exception occurred, which will cause a $status response",ex)
                super.handleExceptionInternal(ex, body, headers, status, request)
            }
        }
    }
}