package io.github.songminkyu.account.entity

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

@Cache(region = "customerCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "seq_customer", allocationSize = 1)
    @Column(name = "customer_id", nullable = false)
    var customerId: Long? = null,

    @Column(nullable = false, length = 100)
    var name: String? = null,

    @Column(nullable = false, length = 100)
    var email: String? = null,

    @Column(name = "mobile_number", nullable = false, length = 20)
    var mobileNumber: String? = null
) : BaseEntity()