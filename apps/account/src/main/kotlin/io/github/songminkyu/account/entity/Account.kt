package io.github.songminkyu.account.entity

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import org.hibernate.envers.Audited
import org.hibernate.proxy.HibernateProxy

@Cache(region = "accountCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(
    name = "account",
    indexes = [Index(name = "idx_account_customer_id", columnList = "customer_id"), Index(
        name = "idx_account_deleted",
        columnList = "deleted"
    )]
)
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE account_number=?")
@Where(clause = "deleted=false")
@Audited
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class Account() : BaseEntity() {
    @Id
    @Column(name = "account_number", nullable = false)
    private var accountNumber: Long? = null

    @Column(name = "customer_id", nullable = false)
    private var customerId: Long? = null

    @Column(name = "account_type", nullable = false, length = 100)
    private var accountType: String? = null

    @Column(name = "branch_address", nullable = false, length = 200)
    private var branchAddress: String? = null

    @Column(name = "communication_sw")
    private var communicationSw: Boolean? = null

    @Column(name = "deleted", nullable = false)
    private var deleted: Boolean = java.lang.Boolean.FALSE

    constructor(accountNumber: Long?, customerId: Long?, accountType: String?, branchAddress: String?, communicationSw: Boolean?, deleted: Boolean) : this() {
        this.accountNumber = accountNumber
        this.customerId = customerId
        this.accountType = accountType
        this.branchAddress = branchAddress
        this.communicationSw = communicationSw
        this.deleted = deleted
    }

    fun getAccountNumber(): Long? = accountNumber
    fun setAccountNumber(accountNumber: Long?) { this.accountNumber = accountNumber }

    fun getCustomerId(): Long? = customerId
    fun setCustomerId(customerId: Long?) { this.customerId = customerId }

    fun getAccountType(): String? = accountType
    fun setAccountType(accountType: String?) { this.accountType = accountType }

    fun getBranchAddress(): String? = branchAddress
    fun setBranchAddress(branchAddress: String?) { this.branchAddress = branchAddress }

    fun getCommunicationSw(): Boolean? = communicationSw
    fun setCommunicationSw(communicationSw: Boolean?) { this.communicationSw = communicationSw }

    fun getDeleted(): Boolean = deleted
    fun setDeleted(deleted: Boolean) { this.deleted = deleted }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        val objEffectiveClass = if (obj is HibernateProxy)
            obj.hibernateLazyInitializer.persistentClass
        else
            obj.javaClass
        val thisEffectiveClass: Class<*>? = if (this is HibernateProxy)
            this.hibernateLazyInitializer.persistentClass
        else
            this.javaClass
        if (thisEffectiveClass != objEffectiveClass) {
            return false
        }
        val account = obj as Account
        return accountNumber != null && accountNumber == account.accountNumber
    }

    override fun hashCode(): Int {
        return if (this is HibernateProxy)
            this.hibernateLazyInitializer.persistentClass.hashCode()
        else
            javaClass.hashCode()
    }

    override fun toString(): String {
        return "Account(accountNumber=$accountNumber, customerId=$customerId, accountType=$accountType, branchAddress=$branchAddress, communicationSw=$communicationSw, deleted=$deleted, ${super.toString()})"
    }

    companion object {
        fun builder(): AccountBuilder = AccountBuilder()
    }

    class AccountBuilder {
        private var accountNumber: Long? = null
        private var customerId: Long? = null
        private var accountType: String? = null
        private var branchAddress: String? = null
        private var communicationSw: Boolean? = null
        private var deleted: Boolean = java.lang.Boolean.FALSE

        fun accountNumber(accountNumber: Long?): AccountBuilder {
            this.accountNumber = accountNumber
            return this
        }

        fun customerId(customerId: Long?): AccountBuilder {
            this.customerId = customerId
            return this
        }

        fun accountType(accountType: String?): AccountBuilder {
            this.accountType = accountType
            return this
        }

        fun branchAddress(branchAddress: String?): AccountBuilder {
            this.branchAddress = branchAddress
            return this
        }

        fun communicationSw(communicationSw: Boolean?): AccountBuilder {
            this.communicationSw = communicationSw
            return this
        }

        fun deleted(deleted: Boolean): AccountBuilder {
            this.deleted = deleted
            return this
        }

        fun build(): Account = Account(accountNumber, customerId, accountType, branchAddress, communicationSw, deleted)
    }
}