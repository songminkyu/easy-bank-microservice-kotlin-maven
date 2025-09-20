package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(
    name = "Customer",
    description = "Schema to hold Customer and Account information"
)
data class CustomerDTO(
    @Schema(
        description = "Name of the customer", example = "Eazy Bytes"
    )
    @field:NotBlank
    @field:Size(min = 5, max = 30)
    val name: String,

    @Schema(
        description = "Email address of the customer", example = "tutor@eazybytes.com"
    )
    @field:NotBlank
    @field:Email
    val email: String,

    @Schema(
        description = "Mobile Number of the customer", example = "9345432123"
    )
    @field:Pattern(regexp = "(^$|\\d{10})", message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}")
    val mobileNumber: String,

    @Schema(
        description = "Account details of the Customer"
    )
    @field:Valid
    val account: AccountDTO
)