package io.github.songminkyu.card.dto

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "card")
data class CardContactInfoDTO(
    var message: String? = null,
    var contactDetails: Map<String, String> = emptyMap(),
    var onCallSupport: List<String> = emptyList()
)