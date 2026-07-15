package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty

@Schema(name = "Card", description = "Schema to hold Card information")
// 💡 @JvmRecord를 제거했습니다.
data class CardDTO(
    // 💡 @param:Schema를 제거하여 필드 기준으로 구성을 맞췄습니다.
    @field:Schema(
        description = "Mobile Number of Customer",
        example = "4354437687"
    ) val mobileNumber: String?,

    @field:Schema(
        description = "Card Number of the customer",
        example = "100646930341"
    ) val cardNumber: @NotEmpty(message = "Card Number can not be a null or empty") String?,

    @field:Schema(
        description = "Type of the card",
        example = "Credit Card"
    ) val cardType: String?,

    @field:Schema(
        description = "Total amount limit available against a card",
        example = "100000"
    ) val totalLimit: Int,

    @field:Schema(
        description = "Total amount used by a Customer",
        example = "1000"
    ) val amountUsed: Int,

    @field:Schema(
        description = "Total available amount against a card",
        example = "90000"
    ) val availableAmount: Int
)