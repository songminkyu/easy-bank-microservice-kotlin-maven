package io.github.songminkyu.account.config

import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import org.springframework.web.reactive.function.client.WebClient

@Configuration(proxyBeanMethods = false)
class GraphQLConfig {

    @Bean
    fun dateTimeScalarType(): GraphQLScalarType {
        return ExtendedScalars.DateTime
    }

    @Bean
    fun graphqlConfigurer(
        graphQLScalarTypes: List<GraphQLScalarType>
    ): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { builder ->
            graphQLScalarTypes.forEach { builder.scalar(it) }
        }
    }

    @Bean
    fun httpGraphQlClientBuilder(
        loadBalancedWebClientBuilder: WebClient.Builder
    ): HttpGraphQlClient.Builder<*> {
        return HttpGraphQlClient.builder(loadBalancedWebClientBuilder)
    }
}