package io.github.songminkyu.loan.config

import io.github.songminkyu.loan.exception.security.SecurityProblemSupport
import io.github.songminkyu.loan.security.AuthoritiesConstants
import io.github.songminkyu.loan.security.oauth2.JwtGrantedAuthorityConverter
import org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig {

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring().requestMatchers(
                "/v3/api-docs/**", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html"
            )
        }
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(
        http: HttpSecurity,
        mvc: MvcRequestMatcher.Builder,
        authenticationConverter: Converter<Jwt, AbstractAuthenticationToken>,
        problemSupport: SecurityProblemSupport
    ): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .headers { headers ->
                headers
                    .contentSecurityPolicy { csp ->
                        csp.policyDirectives("script-src 'self' 'unsafe-inline'")
                    }
                    .frameOptions { it.sameOrigin() }
            }
            .exceptionHandling { exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(problemSupport)
                    .accessDeniedHandler(problemSupport)
            }
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers(toH2Console()).permitAll()
                    .requestMatchers(mvc.pattern("/*/actuator/**")).permitAll()
                    .requestMatchers(HttpMethod.GET).permitAll()
                    .requestMatchers(mvc.pattern("/api/loan/**")).hasAuthority(AuthoritiesConstants.LOAN)
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2
                    .jwt { jwt -> jwt.jwtAuthenticationConverter(authenticationConverter) }
                    .authenticationEntryPoint(problemSupport)
                    .accessDeniedHandler(problemSupport)
            }
            .build()
    }

    @Bean
    fun authenticationConverter(): Converter<Jwt, AbstractAuthenticationToken> {
        return JwtAuthenticationConverter().apply {
            setJwtGrantedAuthoritiesConverter(JwtGrantedAuthorityConverter())
        }
    }

    @Bean
    fun mvc(introspector: HandlerMappingIntrospector): MvcRequestMatcher.Builder {
        return MvcRequestMatcher.Builder(introspector)
    }
}