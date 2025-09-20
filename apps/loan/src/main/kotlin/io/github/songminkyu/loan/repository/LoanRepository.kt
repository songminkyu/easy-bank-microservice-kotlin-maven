package io.github.songminkyu.loan.repository

import io.github.songminkyu.loan.entity.Loan
import jakarta.persistence.QueryHint
import org.hibernate.jpa.HibernateHints.HINT_CACHEABLE
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.stereotype.Repository

@Repository
interface LoanRepository : JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    companion object {
        const val LOAN_BY_MOBILE_NUMBER_CACHE = "loanByMobileNumber"
    }

    @Cacheable(cacheNames = [LOAN_BY_MOBILE_NUMBER_CACHE])
    fun findByMobileNumber(mobileNumber: String): Loan?

    @QueryHints(QueryHint(name = HINT_CACHEABLE, value = "true"))
    fun existsByMobileNumber(mobileNumber: String): Boolean

    fun findByLoanNumber(loanNumber: String): Loan?
}