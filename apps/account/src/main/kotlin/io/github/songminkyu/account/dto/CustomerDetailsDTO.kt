package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "CustomerDetails", description = "Schema to hold Customer, Account, Card and Loan information")
@JvmRecord
data class CustomerDetailsDTO(
    @field:Schema(
        description = "Name of the customer",
        example = "Eazy Bytes"
    ) @param:Schema(
        description = "Name of the customer",
        example = "Eazy Bytes"
    ) val name: String?,
    @field:Schema(
        description = "Email address of the customer",
        example = "tutor@eazybytes.com"
    ) @param:Schema(
        description = "Email address of the customer",
        example = "tutor@eazybytes.com"
    ) val email: String?,
    @field:Schema(
        description = "Mobile Number of the customer",
        example = "9345432123"
    ) @param:Schema(
        description = "Mobile Number of the customer",
        example = "9345432123"
    ) val mobileNumber: String?,

    @field:Schema(description = "Account details of the Customer") @param:Schema(
        description = "Account details of the Customer"
    ) val account: AccountDTO?,
    @field:Schema(description = "Loan details of the Customer") @param:Schema(
        description = "Loan details of the Customer"
    ) val loan: LoanDTO?,

    @field:Schema(description = "Card details of the Customer") @param:Schema(
        description = "Card details of the Customer"
    ) val card: CardDTO?
) 