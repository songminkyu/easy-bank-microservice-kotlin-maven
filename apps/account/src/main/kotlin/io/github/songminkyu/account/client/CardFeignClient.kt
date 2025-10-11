package io.github.songminkyu.account.client

import io.github.songminkyu.account.dto.CardDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "card", fallback = CardFallback::class)
interface CardFeignClient {
    @GetMapping(value = ["/api/card"], consumes = ["application/json"])
    fun fetchCardDetails(
        @RequestHeader("X-Correlation-Id") correlationId: String?,
        @RequestParam mobileNumber: String?
    ): CardDTO?
}