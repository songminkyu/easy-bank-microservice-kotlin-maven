package io.github.songminkyu.card.filter

import io.github.songminkyu.card.constants.Constants.CARD_TYPE
import io.github.songminkyu.card.constants.Constants.TOTAL_LIMIT
import io.github.songminkyu.card.entity.Card
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils
import java.util.*

data class CardFilter(
    var cardType: String? = null,
    var totalLimit: Number? = null
) : Specification<Card> {

    override fun toPredicate(
        root: Root<Card>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val predicates = LinkedList<Predicate>()

        if (StringUtils.hasText(cardType)) {
            predicates.add(criteriaBuilder.equal(root.get<String?>(CARD_TYPE), cardType))
        }

        if (totalLimit != null) {
            predicates.add(criteriaBuilder.ge(root.get<Number?>(TOTAL_LIMIT), totalLimit))
        }

        if (predicates.isEmpty()) {
            return null
        }
        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}