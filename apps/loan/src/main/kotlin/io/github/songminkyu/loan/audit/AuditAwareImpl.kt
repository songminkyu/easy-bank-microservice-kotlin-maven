package io.github.songminkyu.loan.audit

import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component("auditAwareImpl")
class AuditAwareImpl : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of("LOANS_MS")
    }
}