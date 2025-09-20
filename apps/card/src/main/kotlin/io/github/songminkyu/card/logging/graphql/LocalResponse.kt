package io.github.songminkyu.card.logging.graphql

import io.github.songminkyu.card.logging.core.HttpResponse
import io.github.songminkyu.card.logging.core.Origin
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

class LocalResponse(private val response: WebGraphQlResponse) : HttpResponse {
    
    private val headers: HttpHeaders = response.responseHeaders
    private val body: Any = response.toMap()
    
    override fun getOrigin(): Origin = Origin.LOCAL
    
    override fun headers(): HttpHeaders = headers
    
    override fun status(): Int = HttpStatus.OK.value()
    
    override fun body(): ByteArray = ByteArray(0)
    
    override fun getBodyAsObject(): Any = body
}