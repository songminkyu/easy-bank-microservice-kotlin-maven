package io.github.songminkyu.account.dto

import lombok.Getter
import lombok.Setter
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "account")
@Getter
@Setter
class AccountContactInfoDTO {
    private val message: String? = null
    private val contactDetails: MutableMap<String?, String?>? = null
    private val onCallSupport: MutableList<String?>? = null
}
