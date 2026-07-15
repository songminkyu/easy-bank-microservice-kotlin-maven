package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(name = "Account", description = "Schema to hold Account information")
// 💡 @JvmRecord를 제거했습니다.
data class AccountDTO(
    // 💡 @JvmField 및 @param:Schema를 제거하여 코드를 단순화했습니다.
    @field:Schema(
        description = "Account Number of Eazy Bank account",
        example = "3454433243"
    ) val accountNumber: @NotNull Long?,

    @field:Schema(
        description = "Account type of Eazy Bank account",
        example = "Savings"
    ) val accountType: @NotBlank String?,

    @field:Schema(
        description = "Eazy Bank branch address",
        example = "123 NewYork"
    ) val branchAddress: @NotBlank String?
)