package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Response",
    description = "Schema to hold successful response information"
)
data class ResponseDTO(
    @Schema(
        description = "Status code in the response"
    )
    val statusCode: String,

    @Schema(
        description = "Status message in the response"
    )
    val statusMsg: String
)