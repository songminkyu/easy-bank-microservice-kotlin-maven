package io.github.songminkyu.account.service.impl

import io.github.songminkyu.account.constants.AccountConstants
import io.github.songminkyu.account.constants.Constants.RANDOM
import io.github.songminkyu.account.dto.AccountDTO
import io.github.songminkyu.account.dto.AccountsMsgDTO
import io.github.songminkyu.account.dto.CustomerDTO
import io.github.songminkyu.account.entity.Account
import io.github.songminkyu.account.entity.Customer
import io.github.songminkyu.account.entity.RevisionEntity
import io.github.songminkyu.account.exception.CustomerAlreadyExistsException
import io.github.songminkyu.account.exception.EntityNotFoundException
import io.github.songminkyu.account.mapper.AccountMapper
import io.github.songminkyu.account.mapper.CustomerMapper
import io.github.songminkyu.account.repository.AccountRepository
import io.github.songminkyu.account.repository.CustomerRepository
import io.github.songminkyu.account.service.AccountService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.data.history.Revision
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

private val logger = KotlinLogging.logger {}

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val customerRepository: CustomerRepository,
    private val customerMapper: CustomerMapper,
    private val accountMapper: AccountMapper,
    private val streamBridge: StreamBridge
) : AccountService {

    override fun test() {
        val accountsMsgDTO = AccountsMsgDTO(
            accountNumber = 123123L,
            name = "pasongsoso",
            email = "test@naver.com",
            mobileNumber = "040440"
        )
        logger.info { "Sending Communication request for the details: $accountsMsgDTO" }
        val result = streamBridge.send("sendCommunication-out-0", accountsMsgDTO)
        logger.info { "Is the Communication request successfully triggered ? : $result" }
    }

    @Async
    override fun createAccount(customer: CustomerDTO): CompletableFuture<Void> {
        if (customerRepository.existsByMobileNumber(customer.mobileNumber)) {
            throw CustomerAlreadyExistsException("Customer already registered with given mobileNumber ${customer.mobileNumber}")
        }

        val customerEntity = customerMapper.toEntity(customer)
        val savedCustomer = customerRepository.save(customerEntity)

        return CompletableFuture.completedFuture(accountRepository.save(createNewAccount(savedCustomer)))
            .thenAccept { savedAccount -> sendCommunication(savedAccount, savedCustomer) }
    }

    override fun fetchAccount(mobileNumber: String): CustomerDTO {
        val customer = customerRepository.findByMobileNumber(mobileNumber)
            ?: throw EntityNotFoundException(Customer::class.java, "mobileNumber", mobileNumber)

        val account = accountRepository.findByCustomerId(customer.customerId)
            ?: throw EntityNotFoundException(Account::class.java, "customerId", customer.customerId.toString())

        return customerMapper.toDto(customer, account)
    }

    override fun getAccountRevisions(accountNumber: Long): List<AccountDTO> {
        return accountRepository.findRevisions(accountNumber)
            .map { revision -> accountMapper.toDto(revision.entity) }
    }

    override fun geCreatorUsername(accountNumber: Long): String {
        val revision = accountRepository.findRevision(accountNumber, 1)
            ?: throw EntityNotFoundException(Revision::class.java, "accountNumber", accountNumber.toString())

        val metadata = revision.metadata
        val revisionEntity = metadata.delegate as RevisionEntity
        return revisionEntity.username
    }

    override fun updateAccount(accountNumber: Long, customer: CustomerDTO): Boolean {
        val accountDTO = customer.account ?: return false

        val account = accountRepository.findById(accountNumber)
            ?: throw EntityNotFoundException(Account::class.java, "AccountNumber", accountNumber.toString())

        accountMapper.partialUpdate(account, accountDTO)
        account.accountNumber = accountNumber
        val savedAccount = accountRepository.save(account)

        val customerId = savedAccount.customerId
        val customerEntity = customerRepository.findById(customerId)
            ?: throw EntityNotFoundException(Customer::class.java, "CustomerID", customerId.toString())

        customerMapper.partialUpdate(customerEntity, customer)
        customerRepository.save(customerEntity)

        return true
    }

    override fun deleteAccount(mobileNumber: String) {
        val customer = customerRepository.findByMobileNumber(mobileNumber)
            ?: throw EntityNotFoundException(Customer::class.java, "mobileNumber", mobileNumber)

        accountRepository.deleteByCustomerId(customer.customerId)
        customerRepository.deleteById(customer.customerId)
    }

    override fun updateCommunicationStatus(accountNumber: Long): Boolean {
        val account = accountRepository.findById(accountNumber)
            ?: throw EntityNotFoundException(Account::class.java, "AccountNumber", accountNumber.toString())

        account.communicationSw = true
        accountRepository.save(account)
        return true
    }

    private fun createNewAccount(customer: Customer): Account {
        val randomAccNumber = 1000000000L + RANDOM.nextInt(900000000)

        return Account(
            customerId = customer.customerId,
            accountNumber = randomAccNumber,
            accountType = AccountConstants.SAVINGS,
            branchAddress = AccountConstants.ADDRESS,
            deleted = false
        )
    }

    private fun sendCommunication(account: Account, customer: Customer) {
        val accountsMsgDTO = AccountsMsgDTO(
            accountNumber = account.accountNumber!!,
            name = customer.name,
            email = customer.email,
            mobileNumber = customer.mobileNumber
        )

        logger.info { "Sending Communication request for the details: $accountsMsgDTO" }
        val result = streamBridge.send("sendCommunication-out-0", accountsMsgDTO)
        logger.info { "Is the Communication request successfully triggered ? : $result" }
    }
}