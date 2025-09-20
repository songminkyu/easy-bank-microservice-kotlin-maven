package io.github.songminkyu.card.config

import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.validation.rules.OnValidationErrorStrategy
import graphql.validation.rules.ValidationRules
import graphql.validation.schemawiring.ValidationSchemaWiring
import io.github.songminkyu.card.graphql.directive.SchemaDirective
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration(proxyBeanMethods = false)
class GraphQLConfig {

    @Bean
    fun dateTimeScalarType(): GraphQLScalarType {
        return ExtendedScalars.DateTime
    }

    @Bean
    fun validationSchemaDirective(): SchemaDirectiveWiring {
        val validationRules = ValidationRules.newValidationRules()
            .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
            .build()
        return ValidationSchemaWiring(validationRules)
    }

    @Bean
    fun graphqlConfigurer(
        graphQLScalarTypes: List<GraphQLScalarType>,
        validationSchemaDirective: SchemaDirectiveWiring,
        schemaDirectives: List<SchemaDirective>
    ): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { builder ->
            graphQLScalarTypes.forEach { builder.scalar(it) }
            builder.directiveWiring(validationSchemaDirective)
            schemaDirectives.forEach { schemaDirective ->
                builder.directive(schemaDirective.name, schemaDirective.directive)
            }
        }
    }
}