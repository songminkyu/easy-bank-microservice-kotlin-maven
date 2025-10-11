package io.github.songminkyu.account.client

import io.github.songminkyu.account.dto.LoanDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "loan", fallback = LoanFallback::class)
interface LoanFeignClient {
    @GetMapping(value = ["/api/loan"], consumes = ["application/json"])
    fun fetchLoanDetails(
        @RequestHeader("X-Correlation-Id") correlationId: String?,
        @RequestParam mobileNumber: String?
    ): LoanDTO?
}