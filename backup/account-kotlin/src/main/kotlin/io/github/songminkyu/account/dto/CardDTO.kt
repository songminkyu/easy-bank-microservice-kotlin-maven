package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty

@Schema(
    name = "Card",
    description = "Schema to hold Card information"
)
data class CardDTO(
    @Schema(
        description = "Mobile Number of Customer", example = "4354437687"
    )
    val mobileNumber: String,

    @Schema(
        description = "Card Number of the customer", example = "100646930341"
    )
    @field:NotEmpty(message = "Card Number can not be a null or empty")
    val cardNumber: String,

    @Schema(
        description = "Type of the card", example = "Credit Card"
    )
    val cardType: String,

    @Schema(
        description = "Total amount limit available against a card", example = "100000"
    )
    val totalLimit: Int,

    @Schema(
        description = "Total amount used by a Customer", example = "1000"
    )
    val amountUsed: Int,

    @Schema(
        description = "Total available amount against a card", example = "90000"
    )
    val availableAmount: Int
)