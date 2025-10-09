
package io.github.songminkyu.account.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.ConstraintViolation
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Violation(
        @Schema(description = "Object name", example = "loginRequest")
        @JsonProperty("object")
        val objectName: String?,
        @Schema(description = "Field name", example = "login")
        val field: String?,
        @Schema(description = "Violation rejected value", example = "a")
        val rejectedValue: Any?,
        @Schema(description = "Error Message", example = "size must be between 4 and 50")
        val message: String?
) {
    constructor(objectName: String?, message: String?) : this(objectName, null, null, message)

    constructor(error: FieldError) : this(
            error.objectName.replaceFirst("DTO$".toRegex(), ""),
    error.field,
    error.rejectedValue,
    error.defaultMessage
    )

    constructor(error: ObjectError) : this(
            null,
            error.objectName.replaceFirst("DTO$".toRegex(), ""),
            null,
    error.defaultMessage
    )

    constructor(violation: ConstraintViolation<*>) : this(
            null,
            violation.propertyPath.toString(),
    violation.invalidValue,
    violation.message
    )
}