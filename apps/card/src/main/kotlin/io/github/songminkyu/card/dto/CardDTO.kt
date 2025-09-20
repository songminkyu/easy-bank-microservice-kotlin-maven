package io.github.songminkyu.card.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

@Schema(
    name = "Card",
    description = "Schema to hold Card information"
)
data class CardDTO(

    @Schema(
        description = "Mobile Number of Customer", example = "4354437687"
    )
    @field:NotBlank
    @field:Pattern(
        regexp = "(^$|\\d{10})",
        message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}"
    )
    val mobileNumber: String,

    @Schema(
        description = "Card Number of the customer", example = "100646930341"
    )
    @field:NotBlank
    @field:Pattern(
        regexp = "(^$|\\d{12})",
        message = "{jakarta.validation.constraint.CardNumber.Pattern.message}"
    )
    val cardNumber: String,

    @Schema(
        description = "Type of the card", example = "Credit Card"
    )
    @field:NotBlank
    val cardType: String,

    @Schema(
        description = "Total amount limit available against a card", example = "100000"
    )
    @field:Positive
    val totalLimit: Int,

    @Schema(
        description = "Total amount used by a Customer", example = "1000"
    )
    @field:PositiveOrZero
    val amountUsed: Int,

    @Schema(
        description = "Total available amount against a card", example = "90000"
    )
    @field:PositiveOrZero
    val availableAmount: Int
)