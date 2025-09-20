package io.github.songminkyu.loan.mapper.impl

import io.github.songminkyu.loan.dto.LoanDTO
import io.github.songminkyu.loan.entity.Loan
import io.github.songminkyu.loan.mapper.LoanMapper
import org.springframework.stereotype.Component
import javax.annotation.processing.Generated

@Generated(
    value = ["org.mapstruct.ap.MappingProcessor"],
    date = "2025-09-19T22:16:12+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
class LoanMapperImpl : LoanMapper {

    override fun toEntity(dto: LoanDTO): Loan {

        return Loan(
            mobileNumber = dto.mobileNumber,
            loanNumber = dto.loanNumber,
            loanType = dto.loanType,
            totalLoan = dto.totalLoan,
            amountPaid = dto.amountPaid,
            outstandingAmount = dto.outstandingAmount
        )
    }

    override fun toDto(entity: Loan): LoanDTO {

        val mobileNumber = entity.mobileNumber
        val loanNumber = entity.loanNumber
        val loanType = entity.loanType
        val totalLoan = entity.totalLoan
        val amountPaid = entity.amountPaid
        val outstandingAmount = entity.outstandingAmount

        return LoanDTO(
            mobileNumber = mobileNumber,
            loanNumber = loanNumber,
            loanType = loanType,
            totalLoan = totalLoan,
            amountPaid = amountPaid,
            outstandingAmount = outstandingAmount
        )
    }

    override fun toEntity(dtoList: List<LoanDTO>): List<Loan> {

        val list = ArrayList<Loan>(dtoList.size)
        for (loanDTO in dtoList) {
            toEntity(loanDTO)?.let { list.add(it) }
        }

        return list
    }

    override fun toDto(entityList: List<Loan>): List<LoanDTO> {

        val list = ArrayList<LoanDTO>(entityList.size)
        for (loan in entityList) {
            toDto(loan)?.let { list.add(it) }
        }

        return list
    }

    override fun partialUpdate(entity: Loan, dto: LoanDTO) {
        if (dto == null) {
            return
        }

        entity.mobileNumber = dto.mobileNumber
        entity.loanNumber = dto.loanNumber
        entity.loanType = dto.loanType
        entity.totalLoan = dto.totalLoan
        entity.amountPaid = dto.amountPaid
        entity.outstandingAmount = dto.outstandingAmount
    }
}