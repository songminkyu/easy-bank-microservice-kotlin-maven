package io.github.songminkyu.account.security

import lombok.AccessLevel
import lombok.NoArgsConstructor

/**
 * Constants for Spring Security authorities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
object AuthoritiesConstants {
    const val ADMIN: String = "ROLE_ADMIN"

    const val USER: String = "ROLE_USER"

    const val ANONYMOUS: String = "ROLE_ANONYMOUS"

    const val ACCOUNT: String = "ROLE_ACCOUNT"
}
