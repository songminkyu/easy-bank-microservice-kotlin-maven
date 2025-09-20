package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(
    name = "Account",
    description = "Schema to hold Account information"
)
data class AccountDTO(
    @Schema(
        description = "Account Number of Eazy Bank account", example = "3454433243"
    )
    @field:NotNull
    val accountNumber: Long,

    @Schema(
        description = "Account type of Eazy Bank account", example = "Savings"
    )
    @field:NotBlank
    val accountType: String,

    @Schema(
        description = "Eazy Bank branch address", example = "123 NewYork"
    )
    @field:NotBlank
    val branchAddress: String
)