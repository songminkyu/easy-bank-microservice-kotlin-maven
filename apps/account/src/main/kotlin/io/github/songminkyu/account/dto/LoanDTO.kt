package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Loan",
    description = "Schema to hold Loan information"
)
data class LoanDTO(
    @Schema(
        description = "Mobile Number of Customer", example = "4365327698"
    )
    val mobileNumber: String,

    @Schema(
        description = "Loan Number of the customer", example = "548732457654"
    )
    val loanNumber: String,

    @Schema(
        description = "Type of the loan", example = "Home Loan"
    )
    val loanType: String,

    @Schema(
        description = "Total loan amount", example = "100000"
    )
    val totalLoan: Int,

    @Schema(
        description = "Total loan amount paid", example = "1000"
    )
    val amountPaid: Int,

    @Schema(
        description = "Total outstanding amount against a loan", example = "99000"
    )
    val outstandingAmount: Int
)