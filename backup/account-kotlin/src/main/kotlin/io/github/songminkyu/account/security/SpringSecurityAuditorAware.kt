package io.github.songminkyu.account.security

import io.github.songminkyu.account.config.Constants
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class SpringSecurityAuditorAware : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM))
    }
}