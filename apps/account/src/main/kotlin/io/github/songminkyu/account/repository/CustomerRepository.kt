package io.github.songminkyu.account.repository

import io.github.songminkyu.account.entity.Customer
import jakarta.persistence.QueryHint
import org.hibernate.jpa.HibernateHints
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : JpaRepository<Customer?, Long?> {
    @QueryHints(QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    fun existsByMobileNumber(mobileNumber: String?): Boolean

    fun findByMobileNumber(mobileNumber: String?): Optional<Customer?>?
}