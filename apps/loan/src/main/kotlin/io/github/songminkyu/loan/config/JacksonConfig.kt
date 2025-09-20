package io.github.songminkyu.loan.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class JacksonConfig {

    @Bean
    fun javaTimeModule(): JavaTimeModule = JavaTimeModule()
}