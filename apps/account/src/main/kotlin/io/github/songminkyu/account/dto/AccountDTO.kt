package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(name = "Account", description = "Schema to hold Account information")
@JvmRecord
data class AccountDTO(
    @JvmField @field:Schema(
        description = "Account Number of Eazy Bank account",
        example = "3454433243"
    ) @param:Schema(
        description = "Account Number of Eazy Bank account",
        example = "3454433243"
    ) val accountNumber: @NotNull Long?,

    @JvmField @field:Schema(
        description = "Account type of Eazy Bank account",
        example = "Savings"
    ) @param:Schema(
        description = "Account type of Eazy Bank account",
        example = "Savings"
    ) val accountType: @NotBlank String?,

    @JvmField @field:Schema(
        description = "Eazy Bank branch address",
        example = "123 NewYork"
    ) @param:Schema(
        description = "Eazy Bank branch address",
        example = "123 NewYork"
    ) val branchAddress: @NotBlank String?
) 