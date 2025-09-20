package io.github.songminkyu.card.controller

import io.github.songminkyu.card.dto.CardDTO
import io.github.songminkyu.card.service.CardService
import org.reactivestreams.Publisher
import org.springframework.data.domain.Window
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.graphql.data.query.ScrollSubrange
import org.springframework.security.access.prepost.PreAuthorize

@GraphQLController
@PreAuthorize("hasAuthority(T(io.github.songminkyu.card.security.AuthoritiesConstants).CARD)")
class CardGraphQLController(
    private val cardService: CardService
) {

    @QueryMapping
    fun cards(subrange: ScrollSubrange): Window<CardDTO> {
        return cardService.getAllCards(subrange)
    }

    @QueryMapping
    fun card(@Argument mobileNumber: String): CardDTO {
        return cardService.getCard(mobileNumber)
    }

    @MutationMapping
    fun createCard(@Argument mobileNumber: String): CardDTO {
        return cardService.createCard(mobileNumber)
    }

    @MutationMapping
    fun deleteCard(@Argument mobileNumber: String): String {
        cardService.deleteCard(mobileNumber)
        return mobileNumber
    }

    @SubscriptionMapping
    fun onNewCard(): Publisher<CardDTO> {
        return cardService.onNewCard()
    }
}