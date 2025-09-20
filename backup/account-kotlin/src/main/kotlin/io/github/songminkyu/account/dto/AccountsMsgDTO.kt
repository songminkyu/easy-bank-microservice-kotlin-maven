package io.github.songminkyu.account.dto

data class AccountsMsgDTO(
    val accountNumber: Long,
    val name: String,
    val email: String,
    val mobileNumber: String
)