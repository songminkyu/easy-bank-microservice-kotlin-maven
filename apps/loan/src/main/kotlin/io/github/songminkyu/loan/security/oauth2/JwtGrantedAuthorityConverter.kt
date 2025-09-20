package io.github.songminkyu.loan.security.oauth2

import io.github.songminkyu.loan.security.SecurityUtils
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

class JwtGrantedAuthorityConverter : Converter<Jwt, Collection<GrantedAuthority>> {

    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        return SecurityUtils.extractAuthorityFromClaims(jwt.claims)
    }
}