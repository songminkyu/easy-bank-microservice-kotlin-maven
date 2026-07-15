package io.github.songminkyu.account.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Response", description = "Schema to hold successful response information")
// 💡 @JvmRecord를 제거했습니다.
data class ResponseDTO(
    // 💡 @param:Schema를 모두 제거하여 구성을 단순화했습니다.
    @field:Schema(description = "Status code in the response") 
    val statusCode: String?,

    @field:Schema(description = "Status message in the response") 
    val statusMsg: String?
)