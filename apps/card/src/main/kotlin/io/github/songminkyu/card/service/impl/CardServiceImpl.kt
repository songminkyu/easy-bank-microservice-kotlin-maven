package io.github.songminkyu.card.service.impl

import io.github.songminkyu.card.constants.CardConstants
import io.github.songminkyu.card.constants.Constants.RANDOM
import io.github.songminkyu.card.consumer.CardPublisher
import io.github.songminkyu.card.dto.CardDTO
import io.github.songminkyu.card.entity.Card
import io.github.songminkyu.card.exception.CardAlreadyExistsException
import io.github.songminkyu.card.exception.EntityNotFoundException
import io.github.songminkyu.card.mapper.CardMapper
import io.github.songminkyu.card.repository.CardRepository
import io.github.songminkyu.card.service.CardService
import io.reactivex.rxjava3.core.Flowable
import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Window
import org.springframework.graphql.data.query.ScrollSubrange
import org.springframework.stereotype.Service

@Service
class CardServiceImpl(
    private val cardRepository: CardRepository,
    private val cardPublisher: CardPublisher,
    private val cardMapper: CardMapper
) : CardService {

    override fun createCard(mobileNumber: String): CardDTO {
        if (cardRepository.existsByMobileNumber(mobileNumber)) {
            throw CardAlreadyExistsException("Card already registered with given mobileNumber $mobileNumber")
        }
        val card = cardRepository.save(createNewCard(mobileNumber))
        return cardMapper.toDto(card)
    }

    override fun getCard(mobileNumber: String): CardDTO {
        val card = cardRepository.findByMobileNumber(mobileNumber)
            .orElseThrow { EntityNotFoundException(Card::class, "mobileNumber", mobileNumber) }
        return cardMapper.toDto(card)
    }

    override fun updateCard(cardNumber: String, card: CardDTO): Boolean {
        val cardEntity = cardRepository.findByCardNumber(cardNumber)
            .orElseThrow { EntityNotFoundException(Card::class, "cardNumber", cardNumber) }
        cardMapper.partialUpdate(cardEntity, card)
        cardEntity.cardNumber = cardNumber
        cardRepository.save(cardEntity)
        return true
    }

    override fun deleteCard(mobileNumber: String) {
        val card = cardRepository.findByMobileNumber(mobileNumber)
            .orElseThrow { EntityNotFoundException(Card::class, "mobileNumber", mobileNumber) }
        cardRepository.deleteById(card.cardId!!)
    }

    override fun getAllCards(subrange: ScrollSubrange): Window<CardDTO> {
        val scrollPosition = subrange.position().orElse(ScrollPosition.offset())
        val limit = Limit.of(subrange.count().orElse(10))
        val sort = Sort.by("cardId").ascending()
        return cardRepository.findAllBy(scrollPosition, limit, sort)
            .map(cardMapper::toDto)
    }

    override fun onNewCard(): Flowable<CardDTO> {
        return cardPublisher.publisher
    }

    private fun createNewCard(mobileNumber: String): Card {
        val newCard = Card()
        val randomCardNumber = 100000000000L + RANDOM.nextInt(900000000)
        newCard.cardNumber = randomCardNumber.toString()
        newCard.mobileNumber = mobileNumber
        newCard.cardType = CardConstants.CREDIT_CARD
        newCard.totalLimit = CardConstants.NEW_CARD_LIMIT
        newCard.amountUsed = 0
        newCard.availableAmount = CardConstants.NEW_CARD_LIMIT
        return newCard
    }
}