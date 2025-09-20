package io.github.songminkyu.account.filter

import io.github.songminkyu.account.constants.Constants.ACCOUNT_TYPE
import io.github.songminkyu.account.constants.Constants.BRANCH_ADDRESS
import io.github.songminkyu.account.constants.Constants.COMMUNICATION_SW
import io.github.songminkyu.account.entity.Account
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils

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
        val predicates = mutableListOf<Predicate>()

        if (StringUtils.hasText(accountType)) {
            predicates.add(criteriaBuilder.equal(root.get<String>(ACCOUNT_TYPE), accountType))
        }

        if (StringUtils.hasText(branchAddress)) {
            predicates.add(
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(BRANCH_ADDRESS)),
                    "${branchAddress!!.lowercase()}%"
                )
            )
        }

        communicationSw?.let { communicationSw ->
            predicates.add(criteriaBuilder.equal(root.get<String>(COMMUNICATION_SW), communicationSw))
        }

        return if (predicates.isEmpty()) {
            null
        } else {
            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}