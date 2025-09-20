package io.github.songminkyu.account.service

import io.github.songminkyu.account.dto.AccountDTO
import io.github.songminkyu.account.dto.CustomerDTO
import java.util.concurrent.CompletableFuture

interface AccountService {

    fun createAccount(customer: CustomerDTO): CompletableFuture<Void>

    fun fetchAccount(mobileNumber: String): CustomerDTO

    fun getAccountRevisions(accountNumber: Long): List<AccountDTO>

    fun geCreatorUsername(accountNumber: Long): String

    fun updateAccount(accountNumber: Long, customer: CustomerDTO): Boolean

    fun deleteAccount(mobileNumber: String)

    fun updateCommunicationStatus(accountNumber: Long): Boolean

    fun test()
}