package io.github.songminkyu.loan.filter

import io.github.songminkyu.loan.constants.Constants.LOAN_TYPE
import io.github.songminkyu.loan.constants.Constants.TOTAL_LOAN
import io.github.songminkyu.loan.entity.Loan
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils

data class LoanFilter(
    var loanType: String? = null,
    var totalLoan: Number? = null
) : Specification<Loan> {

    override fun toPredicate(
        root: Root<Loan>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val predicates = mutableListOf<Predicate>()

        loanType?.let {
            predicates.add(criteriaBuilder.equal(root.get<String>(LOAN_TYPE), it))
        }

        totalLoan?.let {
            predicates.add(criteriaBuilder.ge(root.get<Number>(TOTAL_LOAN), it))
        }

        return if (predicates.isEmpty()) {
            null
        } else {
            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}