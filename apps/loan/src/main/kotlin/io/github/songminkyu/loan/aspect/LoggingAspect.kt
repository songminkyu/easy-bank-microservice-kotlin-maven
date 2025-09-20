package io.github.songminkyu.loan.aspect

import io.github.songminkyu.loan.constants.Constants
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import java.util.*

@Aspect
class LoggingAspect(private val env: Environment) {

    @Pointcut(
        "within(@org.springframework.stereotype.Repository *)" +
                " || within(@org.springframework.stereotype.Service *)" +
                " || within(@org.springframework.web.bind.annotation.RestController *)"
    )
    fun springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Pointcut(
        "within(io.github.songminkyu.loan.repository..*)" +
                " || within(io.github.songminkyu.loan.service..*)" +
                " || within(io.github.songminkyu.loan.controller..*)"
    )
    fun applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    private fun logger(joinPoint: JoinPoint): Logger {
        return LoggerFactory.getLogger(joinPoint.signature.declaringTypeName)
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    fun logAfterThrowing(joinPoint: JoinPoint, e: Throwable) {
        if (env.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_DEVELOPMENT))) {
            logger(joinPoint).error(
                "Exception in {}() with cause = '{}' and exception = '{}'",
                joinPoint.signature.name,
                e.cause?.toString() ?: "NULL",
                e.message,
                e
            )
        } else {
            logger(joinPoint).error(
                "Exception in {}() with cause = {}",
                joinPoint.signature.name,
                e.cause?.toString() ?: "NULL"
            )
        }
    }

    @Around("applicationPackagePointcut() && springBeanPointcut()")
    @Throws(Throwable::class)
    fun logAround(joinPoint: ProceedingJoinPoint): Any? {
        val log = logger(joinPoint)
        if (log.isDebugEnabled) {
            log.debug(
                "Enter: {}() with argument[s] = {}",
                joinPoint.signature.name,
                Arrays.toString(joinPoint.args)
            )
        }
        try {
            val result = joinPoint.proceed()
            if (log.isDebugEnabled) {
                log.debug("Exit: {}() with result = {}", joinPoint.signature.name, result)
            }
            return result
        } catch (e: IllegalArgumentException) {
            log.error(
                "Illegal argument: {} in {}()",
                Arrays.toString(joinPoint.args),
                joinPoint.signature.name
            )
            throw e
        }
    }
}