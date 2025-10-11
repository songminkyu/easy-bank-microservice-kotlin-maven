package io.github.songminkyu.account.client

import io.github.songminkyu.account.dto.LoanDTO
import org.springframework.stereotype.Component

@Component
class LoanFallback : LoanFeignClient {
    override fun fetchLoanDetails(correlationId: String?, mobileNumber: String?): LoanDTO? {
        return null
    }
}