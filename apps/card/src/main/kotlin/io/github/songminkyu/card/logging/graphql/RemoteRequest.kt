package io.github.songminkyu.card.logging.graphql

import graphql.ExecutionInput
import io.github.songminkyu.card.logging.core.HttpRequest
import io.github.songminkyu.card.logging.core.Origin
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import java.net.URI
import java.util.*

class RemoteRequest(private val request: WebGraphQlRequest) : HttpRequest {
    
    private val headers: HttpHeaders = request.headers
    private val body: Any = request.toMap()
    private val uri: URI = request.uri.toUri()
    private val executionInput: ExecutionInput = request.toExecutionInput()
    
    override fun getOrigin(): Origin = Origin.REMOTE
    
    override fun getRemote(): String = "0:0:0:0:0:0:0:1"
    
    override fun getMethod(): String = HttpMethod.POST.name()
    
    override fun getRequestUri(): URI = uri
    
    override fun headers(): HttpHeaders = headers
    
    override fun body(): ByteArray = ByteArray(0)
    
    override fun getBodyAsObject(): Any = body
    
    override fun getAdditionalContent(): Map<String, Any> {
        val content = LinkedHashMap<String, Any>()
        content["query"] = executionInput.query
        content["variables"] = executionInput.variables
        return content
    }
}