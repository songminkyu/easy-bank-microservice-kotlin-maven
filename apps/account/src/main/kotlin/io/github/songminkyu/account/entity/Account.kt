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
import java.util.*

@Cache(region = "accountCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "account", indexes = [
    Index(name = "idx_account_customer_id", columnList = "customer_id"),
    Index(name = "idx_account_deleted", columnList = "deleted")
])
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE account_number=?")
@Where(clause = "deleted=false")
@Audited
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Account(
    @Id
    @Column(name = "account_number", nullable = false)
    val accountNumber: Long? = null,

    @Column(name = "customer_id", nullable = false)
    val customerId: Long? = null,

    @Column(name = "account_type", nullable = false, length = 100)
    val accountType: String? = null,

    @Column(name = "branch_address", nullable = false, length = 200)
    val branchAddress: String? = null,

    @Column(name = "communication_sw")
    val communicationSw: Boolean? = null,

    @Column(name = "deleted", nullable = false)
    val deleted: Boolean = false
) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val otherEffectiveClass = when (other) {
            is HibernateProxy -> other.hibernateLazyInitializer.persistentClass
            else -> other.javaClass
        }
        val thisEffectiveClass = when (this) {
            is HibernateProxy -> this.hibernateLazyInitializer.persistentClass
            else -> this.javaClass
        }

        if (thisEffectiveClass != otherEffectiveClass) return false

        other as Account
        return accountNumber != null && accountNumber == other.accountNumber
    }

    override fun hashCode(): Int {
        return when (this) {
            is HibernateProxy -> this.hibernateLazyInitializer.persistentClass.hashCode()
            else -> javaClass.hashCode()
        }
    }

    override fun toString(): String {
        return "Account(accountNumber=$accountNumber, customerId=$customerId, accountType='$accountType', branchAddress='$branchAddress', communicationSw=$communicationSw, deleted=$deleted)"
    }
}