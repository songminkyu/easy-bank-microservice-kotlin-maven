package io.github.songminkyu.account.service.impl

import io.github.oshai.kotlinlogging.KotlinLogging
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
import lombok.RequiredArgsConstructor
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.data.history.Revision
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Supplier

@Service
@RequiredArgsConstructor
class AccountServiceImpl : AccountService {
    private val accountRepository: AccountRepository? = null
    private val customerRepository: CustomerRepository? = null
    private val customerMapper: CustomerMapper? = null

    private val accountMapper: AccountMapper? = null

    private val streamBridge: StreamBridge? = null
    private val logger = KotlinLogging.logger {}

    override fun test() {
        val accountsMsgDTO = AccountsMsgDTO(
            123123L, "pasongsoso",
            "test@naver.com", "040440"
        )
        logger.info { "Sending Communication request for the details: $accountsMsgDTO" }
        val result = streamBridge!!.send("sendCommunication-out-0", accountsMsgDTO)
        logger.info { "Is the Communication request successfully triggered ? : $result" }
    }

    @Async
    override fun createAccount(customer: CustomerDTO?): CompletableFuture<Void?>? {
        if (customerRepository!!.existsByMobileNumber(customer?.mobileNumber)) {
            throw CustomerAlreadyExistsException(
                "Customer already registered with given mobileNumber "
                        + customer?.mobileNumber
            )
        }
        val customerEntity = customerMapper!!.toEntity(customer)
        val savedCustomer = customerRepository.save<Customer>(customerEntity as Customer)
        return CompletableFuture.completedFuture<Account?>(
            accountRepository!!.save<Account?>(createNewAccount(savedCustomer))
        )
            .thenAccept(Consumer { savedAccount: Account? -> sendCommunication(savedAccount!!, savedCustomer) })
    }

    override fun fetchAccount(mobileNumber: String?): CustomerDTO? {
        val customer = customerRepository!!.findByMobileNumber(mobileNumber)!!.orElseThrow<EntityNotFoundException?>(
            Supplier { EntityNotFoundException(Customer::class.java, "mobileNumber", mobileNumber) }
        )
        val account =
            accountRepository!!.findByCustomerId(customer!!.getCustomerId())!!.orElseThrow<EntityNotFoundException?>(
                Supplier {
                    EntityNotFoundException(
                        Account::class.java,
                        "customerId",
                        customer.getCustomerId().toString()
                    )
                }
            )
        return customerMapper!!.toDto(customer, account)
    }

    override fun getAccountRevisions(accountNumber: Long?): MutableList<AccountDTO?>? {
        return accountRepository!!.findRevisions(accountNumber)
            .stream()
            .map<AccountDTO?> { revision: Revision<Int, Account?>? -> accountMapper!!.toDto(revision!!.getEntity()) }
            .toList()
    }

    override fun geCreatorUsername(accountNumber: Long?): String? {
        val revision = accountRepository!!.findRevision(accountNumber, 1).orElseThrow<EntityNotFoundException?>(
            Supplier { EntityNotFoundException(Revision::class.java, "accountNumber", accountNumber.toString()) }
        )
        val metadata = revision.getMetadata()
        val revisionEntity = metadata.getDelegate<RevisionEntity>()
        return revisionEntity.getUsername()
    }

    override fun updateAccount(accountNumber: Long?, customer: CustomerDTO?): Boolean {
        var isUpdated = false
        val accountDTO = customer?.account
        if (accountDTO != null) {
            var account = accountRepository!!.findById(accountNumber).orElseThrow<EntityNotFoundException?>(
                Supplier { EntityNotFoundException(Account::class.java, "AccountNumber", accountNumber.toString()) }
            )
            accountMapper!!.partialUpdate(account, accountDTO)
            account!!.setAccountNumber(accountNumber)
            account = accountRepository.save<Account?>(account)

            val customerId = account!!.getCustomerId()
            val customerEntity = customerRepository!!.findById(customerId).orElseThrow<EntityNotFoundException?>(
                Supplier { EntityNotFoundException(Customer::class.java, "CustomerID", customerId.toString()) }
            )
            customerMapper!!.partialUpdate(customerEntity, customer)
            customerRepository.save<Customer?>(customerEntity)
            isUpdated = true
        }
        return isUpdated
    }

    override fun deleteAccount(mobileNumber: String?) {
        val customer = customerRepository!!.findByMobileNumber(mobileNumber)!!.orElseThrow<EntityNotFoundException?>(
            Supplier { EntityNotFoundException(Customer::class.java, "mobileNumber", mobileNumber) }
        )
        accountRepository!!.deleteByCustomerId(customer!!.getCustomerId())
        customerRepository.deleteById(customer.getCustomerId())
    }

    override fun updateCommunicationStatus(accountNumber: Long?): Boolean {
        var isUpdated = false
        if (accountNumber != null) {
            val account = accountRepository!!.findById(accountNumber).orElseThrow<EntityNotFoundException?>(
                Supplier { EntityNotFoundException(Account::class.java, "AccountNumber", accountNumber.toString()) }
            )
            account!!.setCommunicationSw(true)
            accountRepository.save<Account?>(account)
            isUpdated = true
        }
        return isUpdated
    }

    private fun createNewAccount(customer: Customer): Account {
        val newAccount = Account()
        newAccount.setCustomerId(customer.getCustomerId())
        val randomAccNumber: Long = 1000000000L + RANDOM.nextInt(900000000)
        newAccount.setAccountNumber(randomAccNumber)
        newAccount.setAccountType(AccountConstants.SAVINGS)
        newAccount.setBranchAddress(AccountConstants.ADDRESS)
        newAccount.setDeleted(false)
        return newAccount
    }

    private fun sendCommunication(account: Account, customer: Customer) {
        val accountsMsgDTO = AccountsMsgDTO(
            account.getAccountNumber(), customer.getName(),
            customer.getEmail(), customer.getMobileNumber()
        )
        logger.info { "Sending Communication request for the details: $accountsMsgDTO" }
        val result = streamBridge!!.send("sendCommunication-out-0", accountsMsgDTO)
        logger.info { "Is the Communication request successfully triggered ? : $result" }
    }
}
