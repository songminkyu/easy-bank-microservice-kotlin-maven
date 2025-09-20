package io.github.songminkyu.account.client

import io.github.songminkyu.account.dto.CardDTO
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Component

@Component
class CardGraphQlClient(httpGraphQlClientBuilder: HttpGraphQlClient.Builder<*>) {

    private val client: HttpGraphQlClient = httpGraphQlClientBuilder
        .url("http://card/graphql")
        .build()

    fun fetchCardDetails(mobileNumber: String): CardDTO? {
        return client.documentName("getCardDetails")
            .variable("mobileNumber", mobileNumber)
            .retrieve("card")
            .toEntity(CardDTO::class.java)
            .block()
    }
}