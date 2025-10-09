package io.github.songminkyu.account.mapper.impl

import io.github.songminkyu.account.dto.*
import io.github.songminkyu.account.entity.Account
import io.github.songminkyu.account.entity.Customer
import io.github.songminkyu.account.mapper.AccountMapper
import io.github.songminkyu.account.mapper.CustomerMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CustomerMapperImpl @Autowired constructor(private val accountMapper: AccountMapper) : CustomerMapper {
    override fun toEntity(dto: CustomerDTO?): Customer? {
        if (dto == null) {
            return null
        }

        val customer = Customer.builder()

        customer.name(dto.name)
        customer.email(dto.email)
        customer.mobileNumber(dto.mobileNumber)

        return customer.build()
    }

    override fun toDto(entity: Customer?): CustomerDTO? {
        if (entity == null) {
            return null
        }

        var name: String? = null
        var email: String? = null
        var mobileNumber: String? = null

        name = entity.getName()
        email = entity.getEmail()
        mobileNumber = entity.getMobileNumber()

        val account: AccountDTO? = null

        val customerDTO = CustomerDTO(name, email, mobileNumber, account)

        return customerDTO
    }

    override fun toEntity(dtoList: MutableList<CustomerDTO?>?): MutableList<Customer?>? {
        if (dtoList == null) {
            return null
        }

        val list: MutableList<Customer?> = ArrayList<Customer?>(dtoList.size)
        for (customerDTO in dtoList) {
            list.add(toEntity(customerDTO))
        }

        return list
    }

    override fun toDto(entityList: MutableList<Customer?>?): MutableList<CustomerDTO?>? {
        if (entityList == null) {
            return null
        }

        val list: MutableList<CustomerDTO?> = ArrayList<CustomerDTO?>(entityList.size)
        for (customer in entityList) {
            list.add(toDto(customer))
        }

        return list
    }

    override fun partialUpdate(entity: Customer?, dto: CustomerDTO?) {
        if (dto == null) {
            return
        }

        if (dto.name != null) {
            entity?.setName(dto.name)
        }
        if (dto.email != null) {
            entity?.setEmail(dto.email)
        }
        if (dto.mobileNumber != null) {
            entity?.setMobileNumber(dto.mobileNumber)
        }
    }

    override fun toDto(customer: Customer?, account: Account?): CustomerDTO? {
        if (customer == null && account == null) {
            return null
        }

        var name: String? = null
        var email: String? = null
        var mobileNumber: String? = null
        if (customer != null) {
            name = customer.getName()
            email = customer.getEmail()
            mobileNumber = customer.getMobileNumber()
        }
        var account1: AccountDTO? = null
        account1 = accountMapper.toDto(account)

        val customerDTO = CustomerDTO(name, email, mobileNumber, account1)

        return customerDTO
    }

    override fun toCustomerDetailsDTO(
        customer: Customer?,
        account: Account?,
        loan: LoanDTO?,
        card: CardDTO?
    ): CustomerDetailsDTO? {
        if (customer == null && account == null && loan == null && card == null) {
            return null
        }

        var mobileNumber: String? = null
        var name: String? = null
        var email: String? = null
        if (customer != null) {
            mobileNumber = customer.getMobileNumber()
            name = customer.getName()
            email = customer.getEmail()
        }
        var account1: AccountDTO? = null
        account1 = accountMapper.toDto(account)
        var loan1: LoanDTO? = null
        loan1 = loan
        var card1: CardDTO? = null
        card1 = card

        val customerDetailsDTO = CustomerDetailsDTO(name, email, mobileNumber, account1, loan1, card1)

        return customerDetailsDTO
    }
}
