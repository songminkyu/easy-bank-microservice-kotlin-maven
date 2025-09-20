package io.github.songminkyu.account.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

object SecurityUtils {
    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    fun getCurrentUserLogin(): Optional<String> {
        val securityContext = SecurityContextHolder.getContext()
        return Optional.ofNullable(extractPrincipal(securityContext.authentication))
    }

    private fun extractPrincipal(authentication: Authentication?): String? {
        return when {
            authentication == null -> null
            authentication.principal is UserDetails -> {
                (authentication.principal as UserDetails).username
            }
            authentication is JwtAuthenticationToken -> {
                authentication.token.claims["preferred_username"] as? String
            }
            authentication.principal is DefaultOidcUser -> {
                val attributes = (authentication.principal as DefaultOidcUser).attributes
                if (attributes.containsKey("preferred_username")) {
                    attributes["preferred_username"] as? String
                } else null
            }
            authentication.principal is String -> authentication.principal as String
            else -> null
        }
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    fun isAuthenticated(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication != null && getAuthorities(authentication).none { it == AuthoritiesConstants.ANONYMOUS }
    }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
    fun hasCurrentUserAnyOfAuthorities(vararg authorities: String): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication != null && getAuthorities(authentication).any { authority ->
            authorities.contains(authority)
        }
    }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
    fun hasCurrentUserNoneOfAuthorities(vararg authorities: String): Boolean {
        return !hasCurrentUserAnyOfAuthorities(*authorities)
    }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    fun hasCurrentUserThisAuthority(authority: String): Boolean {
        return hasCurrentUserAnyOfAuthorities(authority)
    }

    private fun getAuthorities(authentication: Authentication): Sequence<String> {
        val authorities = if (authentication is JwtAuthenticationToken) {
            extractAuthorityFromClaims(authentication.token.claims)
        } else {
            authentication.authorities
        }
        return authorities.asSequence().map { it.authority }
    }

    fun extractAuthorityFromClaims(claims: Map<String, Any?>): List<GrantedAuthority> {
        @Suppress("UNCHECKED_CAST")
        val realmAccess = claims["realm_access"] as? Map<String, Any?>
        return if (realmAccess != null) {
            mapRolesToGrantedAuthorities(getRolesFromClaims(claims))
        } else {
            emptyList()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getRolesFromClaims(claims: Map<String, Any?>): Collection<String> {
        return claims["roles"] as? Collection<String> ?: emptyList()
    }

    private fun mapRolesToGrantedAuthorities(roles: Collection<String>): List<GrantedAuthority> {
        return roles.map { roleName ->
            SimpleGrantedAuthority("ROLE_$roleName")
        }
    }
}