package io.github.songminkyu.account.dto

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "account")
data class AccountContactInfoDTO(
    var message: String? = null,
    var contactDetails: Map<String, String> = emptyMap(),
    var onCallSupport: List<String> = emptyList()
)