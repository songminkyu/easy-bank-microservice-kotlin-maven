package io.github.songminkyu.card.entity

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

@Cache(region = "cardCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class Card : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "seq_card", allocationSize = 1)
    @Column(name = "card_id", nullable = false)
    var cardId: Long? = null

    @Column(name = "mobile_number", nullable = false, length = 15)
    var mobileNumber: String? = null

    @Column(name = "card_number", nullable = false, length = 100)
    var cardNumber: String? = null

    @Column(name = "card_type", nullable = false, length = 100)
    var cardType: String? = null

    @Column(name = "total_limit", nullable = false)
    var totalLimit: Int = 0

    @Column(name = "amount_used", nullable = false)
    var amountUsed: Int = 0

    @Column(name = "available_amount", nullable = false)
    var availableAmount: Int = 0

    override fun toString(): String {
        return "Card(cardId=$cardId, mobileNumber=$mobileNumber, cardNumber=$cardNumber, cardType=$cardType, totalLimit=$totalLimit, amountUsed=$amountUsed, availableAmount=$availableAmount)"
    }
}