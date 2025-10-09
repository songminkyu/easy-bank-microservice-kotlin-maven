package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Loan", description = "Schema to hold Loan information")
@JvmRecord
data class LoanDTO(
    @field:Schema(
        description = "Mobile Number of Customer",
        example = "4365327698"
    ) @param:Schema(
        description = "Mobile Number of Customer",
        example = "4365327698"
    ) val mobileNumber: String?,

    @field:Schema(
        description = "Loan Number of the customer",
        example = "548732457654"
    ) @param:Schema(
        description = "Loan Number of the customer",
        example = "548732457654"
    ) val loanNumber: String?,

    @field:Schema(
        description = "Type of the loan",
        example = "Home Loan"
    ) @param:Schema(
        description = "Type of the loan",
        example = "Home Loan"
    ) val loanType: String?,

    @field:Schema(
        description = "Total loan amount",
        example = "100000"
    ) @param:Schema(
        description = "Total loan amount",
        example = "100000"
    ) val totalLoan: Int,

    @field:Schema(
        description = "Total loan amount paid",
        example = "1000"
    ) @param:Schema(
        description = "Total loan amount paid",
        example = "1000"
    ) val amountPaid: Int,

    @field:Schema(
        description = "Total outstanding amount against a loan",
        example = "99000"
    ) @param:Schema(
        description = "Total outstanding amount against a loan",
        example = "99000"
    ) val outstandingAmount: Int
) 