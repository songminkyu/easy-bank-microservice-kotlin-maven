package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import java.time.Instant

@Schema(name = "ErrorResponse", description = "Schema to hold error response information")
@JvmRecord
data class ErrorResponseDTO(
    @field:Schema(description = "API path invoked by client") @param:Schema(
        description = "API path invoked by client"
    ) val apiPath: String?,

    @field:Schema(description = "Error code representing the error happened") @param:Schema(
        description = "Error code representing the error happened"
    ) val errorCode: HttpStatus?,

    @field:Schema(description = "Error message representing the error happened") @param:Schema(
        description = "Error message representing the error happened"
    ) val errorMessage: String?,

    @field:Schema(description = "Time representing when the error happened") @param:Schema(
        description = "Time representing when the error happened"
    ) val errorTime: Instant?
)
