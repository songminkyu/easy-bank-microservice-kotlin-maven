package io.github.songminkyu.card.mapper.impl

import io.github.songminkyu.card.dto.CardDTO
import io.github.songminkyu.card.entity.Card
import io.github.songminkyu.card.mapper.CardMapper
import org.springframework.stereotype.Component
import javax.annotation.processing.Generated

@Generated(
    value = ["org.mapstruct.ap.MappingProcessor"],
    date = "2025-09-20T10:07:26+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
class CardMapperImpl : CardMapper {

    override fun toEntity(dto: CardDTO): Card {
        val card = Card()

        card.mobileNumber = dto.mobileNumber
        card.cardNumber = dto.cardNumber
        card.cardType = dto.cardType
        card.totalLimit = dto.totalLimit
        card.amountUsed = dto.amountUsed
        card.availableAmount = dto.availableAmount

        return card
    }

    override fun toDto(entity: Card): CardDTO {

        val mobileNumber: String = entity.mobileNumber ?: ""
        val cardNumber: String = entity.cardNumber ?: ""
        val cardType: String = entity.cardType ?: ""
        val totalLimit: Int = entity.totalLimit
        val amountUsed: Int = entity.amountUsed
        val availableAmount: Int = entity.availableAmount

        return CardDTO(
            mobileNumber = mobileNumber,
            cardNumber = cardNumber,
            cardType = cardType,
            totalLimit = totalLimit,
            amountUsed = amountUsed,
            availableAmount = availableAmount
        )
    }

    override fun toEntity(dtoList: List<CardDTO>): List<Card> {

        val list = ArrayList<Card>(dtoList.size)
        for (cardDTO in dtoList) {
            list.add(toEntity(cardDTO))
        }

        return list
    }

    override fun toDto(entityList: List<Card>): List<CardDTO> {

        val list = ArrayList<CardDTO>(entityList.size)
        for (card in entityList) {
            list.add(toDto(card))
        }

        return list
    }

    override fun partialUpdate(entity: Card, dto: CardDTO) {
        if (dto == null) {
            return
        }

        dto.mobileNumber?.let { entity.mobileNumber = it }
        dto.cardNumber?.let { entity.cardNumber = it }
        dto.cardType?.let { entity.cardType = it }

        entity.totalLimit = dto.totalLimit
        entity.amountUsed = dto.amountUsed
        entity.availableAmount = dto.availableAmount
    }
}