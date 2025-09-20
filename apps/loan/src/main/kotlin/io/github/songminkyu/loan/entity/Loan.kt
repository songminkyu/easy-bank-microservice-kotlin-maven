package io.github.songminkyu.loan.entity

import jakarta.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

@Cache(region = "loanCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
data class Loan(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "seq_loan", allocationSize = 1)
    @Column(name = "loan_id", nullable = false)
    var loanId: Long? = null,

    @Column(name = "mobile_number", nullable = false, length = 15)
    var mobileNumber: String = "",

    @Column(name = "loan_number", nullable = false, length = 100)
    var loanNumber: String = "",

    @Column(name = "loan_type", nullable = false, length = 100)
    var loanType: String = "",

    @Column(name = "total_loan", nullable = false)
    var totalLoan: Int = 0,

    @Column(name = "amount_paid", nullable = false)
    var amountPaid: Int = 0,

    @Column(name = "outstanding_amount", nullable = false)
    var outstandingAmount: Int = 0

) : BaseEntity()