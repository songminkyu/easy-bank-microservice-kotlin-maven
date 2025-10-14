package io.github.songminkyu.account.security.oauth2

import io.github.songminkyu.account.security.SecurityUtils
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

class JwtGrantedAuthorityConverter : Converter<Jwt, MutableCollection<GrantedAuthority>> {

    override fun convert(jwt: Jwt): MutableCollection<GrantedAuthority> {
        return SecurityUtils.extractAuthorityFromClaims(jwt.claims)
    }
}