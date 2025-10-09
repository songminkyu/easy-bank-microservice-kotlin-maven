package io.github.songminkyu.account.entity

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

@Cache(region = "customerCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class Customer() : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "seq_customer", allocationSize = 1)
    @Column(name = "customer_id", nullable = false)
    private var customerId: Long? = null

    @Column(nullable = false, length = 100)
    private var name: String? = null

    @Column(nullable = false, length = 100)
    private var email: String? = null

    @Column(name = "mobile_number", nullable = false, length = 20)
    private var mobileNumber: String? = null

    constructor(customerId: Long?, name: String?, email: String?, mobileNumber: String?) : this() {
        this.customerId = customerId
        this.name = name
        this.email = email
        this.mobileNumber = mobileNumber
    }

    fun getCustomerId(): Long? = customerId
    fun setCustomerId(customerId: Long?) { this.customerId = customerId }

    fun getName(): String? = name
    fun setName(name: String?) { this.name = name }

    fun getEmail(): String? = email
    fun setEmail(email: String?) { this.email = email }

    fun getMobileNumber(): String? = mobileNumber
    fun setMobileNumber(mobileNumber: String?) { this.mobileNumber = mobileNumber }

    override fun toString(): String {
        return "Customer(customerId=$customerId, name=$name, email=$email, mobileNumber=$mobileNumber, ${super.toString()})"
    }

    companion object {
        fun builder(): CustomerBuilder = CustomerBuilder()
    }

    class CustomerBuilder {
        private var customerId: Long? = null
        private var name: String? = null
        private var email: String? = null
        private var mobileNumber: String? = null

        fun customerId(customerId: Long?): CustomerBuilder {
            this.customerId = customerId
            return this
        }

        fun name(name: String?): CustomerBuilder {
            this.name = name
            return this
        }

        fun email(email: String?): CustomerBuilder {
            this.email = email
            return this
        }

        fun mobileNumber(mobileNumber: String?): CustomerBuilder {
            this.mobileNumber = mobileNumber
            return this
        }

        fun build(): Customer = Customer(customerId, name, email, mobileNumber)
    }
}