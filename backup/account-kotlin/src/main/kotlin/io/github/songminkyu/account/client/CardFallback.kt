package io.github.songminkyu.account.client

import io.github.songminkyu.account.dto.CardDTO
import org.springframework.stereotype.Component

@Component
class CardFallback : CardFeignClient {
    override fun fetchCardDetails(correlationId: String, mobileNumber: String): CardDTO? {
        return null
    }
}