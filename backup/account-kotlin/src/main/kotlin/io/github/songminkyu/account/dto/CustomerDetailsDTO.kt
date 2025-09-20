package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "CustomerDetails",
    description = "Schema to hold Customer, Account, Card and Loan information"
)
data class CustomerDetailsDTO(
    @Schema(description = "Name of the customer", example = "Eazy Bytes")
    val name: String,

    @Schema(description = "Email address of the customer", example = "tutor@eazybytes.com")
    val email: String,

    @Schema(description = "Mobile Number of the customer", example = "9345432123")
    val mobileNumber: String,

    @Schema(description = "Account details of the Customer")
    val account: AccountDTO,

    @Schema(description = "Loan details of the Customer")
    val loan: LoanDTO?,

    @Schema(description = "Card details of the Customer")
    val card: CardDTO?
)