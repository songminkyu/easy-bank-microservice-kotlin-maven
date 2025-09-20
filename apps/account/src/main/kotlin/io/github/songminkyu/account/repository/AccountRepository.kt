package io.github.songminkyu.account.repository

import io.github.songminkyu.account.entity.Account
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.history.RevisionRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface AccountRepository : JpaRepository<Account, Long>,
    RevisionRepository<Account, Long, Int>,
    JpaSpecificationExecutor<Account> {

    companion object {
        const val ACCOUNT_BY_CUSTOMER_ID_CACHE = "accountByCustomerId"
    }

    @Cacheable(cacheNames = [ACCOUNT_BY_CUSTOMER_ID_CACHE])
    fun findByCustomerId(customerId: Long): Optional<Account>

    @Transactional
    @Modifying
    fun deleteByCustomerId(customerId: Long)
}