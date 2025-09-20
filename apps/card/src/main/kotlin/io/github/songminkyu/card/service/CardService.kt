package io.github.songminkyu.card.service

import io.github.songminkyu.card.dto.CardDTO
import io.reactivex.rxjava3.core.Flowable
import org.springframework.data.domain.Window
import org.springframework.graphql.data.query.ScrollSubrange

interface CardService {

    fun createCard(mobileNumber: String): CardDTO

    fun getCard(mobileNumber: String): CardDTO

    fun updateCard(cardNumber: String, card: CardDTO): Boolean

    fun deleteCard(mobileNumber: String)

    fun getAllCards(subrange: ScrollSubrange): Window<CardDTO>

    fun onNewCard(): Flowable<CardDTO>
}