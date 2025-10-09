package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(name = "Customer", description = "Schema to hold Customer and Account information")
@JvmRecord
data class CustomerDTO(
    @JvmField @field:Schema(
        description = "Name of the customer",
        example = "Eazy Bytes"
    ) @param:Schema(
        description = "Name of the customer",
        example = "Eazy Bytes"
    ) val name: @NotBlank @Size(min = 5, max = 30) String?,

    @JvmField @field:Schema(
        description = "Email address of the customer",
        example = "tutor@eazybytes.com"
    ) @param:Schema(
        description = "Email address of the customer",
        example = "tutor@eazybytes.com"
    ) val email: @NotBlank @Email String?,

    @JvmField @field:Schema(
        description = "Mobile Number of the customer",
        example = "9345432123"
    ) @param:Schema(
        description = "Mobile Number of the customer",
        example = "9345432123"
    ) val mobileNumber: @Pattern(
        regexp = "(^$|\\d{10})",
        message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}"
    ) String?,

    @JvmField @field:Schema(description = "Account details of the Customer") @param:Schema(
        description = "Account details of the Customer"
    ) val account: @Valid AccountDTO?
) 