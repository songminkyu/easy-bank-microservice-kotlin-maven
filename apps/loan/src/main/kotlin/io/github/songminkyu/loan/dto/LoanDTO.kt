package io.github.songminkyu.loan.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

@Schema(
    name = "Loan",
    description = "Schema to hold Loan information"
)
data class LoanDTO(

    @Schema(
        description = "Mobile Number of Customer",
        example = "4365327698"
    )
    @field:NotBlank
    @field:Pattern(
        regexp = "(^$|\\d{10})",
        message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}"
    )
    val mobileNumber: String,

    @Schema(
        description = "Loan Number of the customer",
        example = "548732457654"
    )
    @field:NotBlank
    @field:Pattern(
        regexp = "(^$|\\d{12})",
        message = "{jakarta.validation.constraint.LoanNumber.Pattern.message}"
    )
    val loanNumber: String,

    @Schema(
        description = "Type of the loan",
        example = "Home Loan"
    )
    @field:NotBlank
    val loanType: String,

    @Schema(
        description = "Total loan amount",
        example = "100000"
    )
    @field:Positive
    val totalLoan: Int,

    @Schema(
        description = "Total loan amount paid",
        example = "1000"
    )
    @field:PositiveOrZero
    val amountPaid: Int,

    @Schema(
        description = "Total outstanding amount against a loan",
        example = "99000"
    )
    @field:PositiveOrZero
    val outstandingAmount: Int
)