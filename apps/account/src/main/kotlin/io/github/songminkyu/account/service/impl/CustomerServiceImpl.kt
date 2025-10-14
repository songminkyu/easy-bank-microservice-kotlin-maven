package io.github.songminkyu.account.service.impl

import io.github.songminkyu.account.client.CardGraphQlClient
import io.github.songminkyu.account.client.LoanFeignClient
import io.github.songminkyu.account.dto.CustomerDetailsDTO
import io.github.songminkyu.account.entity.Account
import io.github.songminkyu.account.entity.Customer
import io.github.songminkyu.account.exception.EntityNotFoundException
import io.github.songminkyu.account.mapper.CustomerMapper
import io.github.songminkyu.account.repository.AccountRepository
import io.github.songminkyu.account.repository.CustomerRepository
import io.github.songminkyu.account.service.CustomerService
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    private val accountRepository: AccountRepository,
    private val customerRepository: CustomerRepository,
    private val cardGraphQlClient: CardGraphQlClient,
    private val loanFeignClient: LoanFeignClient,
    private val customerMapper: CustomerMapper
) : CustomerService {

    override fun fetchCustomerDetails(mobileNumber: String?, correlationId: String?): CustomerDetailsDTO? {
        val customer = customerRepository.findByMobileNumber(mobileNumber)
            ?.orElseThrow {
                EntityNotFoundException(Customer::class.java, "mobileNumber", mobileNumber)
            }

        val account = accountRepository.findByCustomerId(customer?.getCustomerId())
            ?.orElseThrow {
                EntityNotFoundException(
                    Account::class.java,
                    "customerId",
                    customer?.getCustomerId().toString()
                )
            }

        val loan = loanFeignClient.fetchLoanDetails(correlationId, mobileNumber)

        val card = cardGraphQlClient.fetchCardDetails(mobileNumber)

        return customerMapper.toCustomerDetailsDTO(customer, account, loan, card)
    }
}