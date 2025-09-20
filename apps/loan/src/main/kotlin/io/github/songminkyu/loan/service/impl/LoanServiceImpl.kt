package io.github.songminkyu.loan.service.impl

import io.github.songminkyu.loan.constants.Constants.RANDOM
import io.github.songminkyu.loan.constants.LoanConstants
import io.github.songminkyu.loan.dto.LoanDTO
import io.github.songminkyu.loan.entity.Loan
import io.github.songminkyu.loan.exception.EntityNotFoundException
import io.github.songminkyu.loan.exception.LoanAlreadyExistsException
import io.github.songminkyu.loan.mapper.LoanMapper
import io.github.songminkyu.loan.repository.LoanRepository
import io.github.songminkyu.loan.service.LoanService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
class LoanServiceImpl(
    private val loanRepository: LoanRepository,
    private val loanMapper: LoanMapper
) : LoanService {

    override fun createLoan(mobileNumber: String) {
        if (loanRepository.existsByMobileNumber(mobileNumber)) {
            throw LoanAlreadyExistsException("Loan already registered with given mobileNumber $mobileNumber")
        }
        loanRepository.save(createNewLoan(mobileNumber))
    }

    override fun fetchLoan(mobileNumber: String): LoanDTO {
        val loan = loanRepository.findByMobileNumber(mobileNumber)
            ?: throw EntityNotFoundException(Loan::class.java, "mobileNumber", mobileNumber)
        return loanMapper.toDto(loan)
    }

    override fun updateLoan(loanNumber: String, loan: LoanDTO): Boolean {
        val loanEntity = loanRepository.findByLoanNumber(loanNumber)
            ?: throw EntityNotFoundException(Loan::class.java, "loanNumber", loanNumber)
        loanMapper.partialUpdate(loanEntity, loan)
        loanEntity.loanNumber = loanNumber
        loanRepository.save(loanEntity)
        return true
    }

    override fun deleteLoan(mobileNumber: String) {
        val loan = loanRepository.findByMobileNumber(mobileNumber)
            ?: throw EntityNotFoundException(Loan::class.java, "mobileNumber", mobileNumber)
        loanRepository.deleteById(loan.loanId!!)
    }

    private fun createNewLoan(mobileNumber: String): Loan {
        val randomLoanNumber = 100000000000L + RANDOM.nextInt(900000000)
        return Loan(
            mobileNumber = mobileNumber,
            loanNumber = randomLoanNumber.toString(),
            loanType = LoanConstants.HOME_LOAN,
            totalLoan = LoanConstants.NEW_LOAN_LIMIT,
            amountPaid = 0,
            outstandingAmount = LoanConstants.NEW_LOAN_LIMIT
        )
    }
}