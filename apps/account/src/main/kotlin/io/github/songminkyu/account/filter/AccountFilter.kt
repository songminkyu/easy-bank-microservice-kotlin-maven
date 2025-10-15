
package io.github.songminkyu.account.filter

import io.github.songminkyu.account.entity.Account
import io.github.songminkyu.account.constants.Constants.ACCOUNT_TYPE
import io.github.songminkyu.account.constants.Constants.BRANCH_ADDRESS
import io.github.songminkyu.account.constants.Constants.COMMUNICATION_SW
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils
import java.util.LinkedList

data class AccountFilter(
        var accountType: String? = null,
        var branchAddress: String? = null,
        var communicationSw: Boolean? = null
) : Specification<Account> {

    override fun toPredicate(
            root: Root<Account>,
    query: CriteriaQuery<*>,
            criteriaBuilder: CriteriaBuilder
    ): Predicate? {
            val predicates = LinkedList<Predicate>()

    if (StringUtils.hasText(accountType)) {
        predicates.add(criteriaBuilder.equal(root.get<String>(ACCOUNT_TYPE), accountType))
    }

    if (StringUtils.hasText(branchAddress)) {
        predicates.add(
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get<String>(BRANCH_ADDRESS)),
                        "${branchAddress!!.lowercase()}%"
                )
        )
    }

    if (communicationSw != null) {
        predicates.add(criteriaBuilder.equal(root.get<Boolean>(COMMUNICATION_SW), communicationSw))
    }

    if (predicates.isEmpty()) {
        return null
    }
    return criteriaBuilder.and(*predicates.toTypedArray())
    }
}