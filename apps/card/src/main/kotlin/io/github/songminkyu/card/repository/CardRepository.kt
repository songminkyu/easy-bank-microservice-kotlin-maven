package io.github.songminkyu.card.repository

import io.github.songminkyu.card.entity.Card
import jakarta.persistence.QueryHint
import org.hibernate.jpa.HibernateHints.HINT_CACHEABLE
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Window
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CardRepository : JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

    companion object {
        const val CARD_BY_MOBILE_NUMBER_CACHE = "cardByMobileNumber"
    }

    @Cacheable(cacheNames = [CARD_BY_MOBILE_NUMBER_CACHE])
    fun findByMobileNumber(mobileNumber: String): Optional<Card>

    @QueryHints(QueryHint(name = HINT_CACHEABLE, value = "true"))
    fun existsByMobileNumber(mobileNumber: String): Boolean

    fun findByCardNumber(cardNumber: String): Optional<Card>

    fun findAllBy(position: ScrollPosition, limit: Limit, sort: Sort): Window<Card>
}