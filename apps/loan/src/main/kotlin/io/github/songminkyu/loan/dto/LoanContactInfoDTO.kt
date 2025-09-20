package io.github.songminkyu.loan.dto

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "loan")
data class LoanContactInfoDTO(
    var message: String = "",
    var contactDetails: Map<String, String> = emptyMap(),
    var onCallSupport: List<String> = emptyList()
)