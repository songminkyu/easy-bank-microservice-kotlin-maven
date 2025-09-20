package io.github.songminkyu.loan.service

import io.github.songminkyu.loan.dto.LoanDTO

interface LoanService {

    fun createLoan(mobileNumber: String)

    fun fetchLoan(mobileNumber: String): LoanDTO

    fun updateLoan(loanNumber: String, loan: LoanDTO): Boolean

    fun deleteLoan(mobileNumber: String)
}