package com.jlima.bookstoremanager.config.audit

import org.springframework.data.domain.AuditorAware
import java.util.Optional

class CustomAuditAware : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of("Jonathan Lima").filter { it.isNotEmpty() }
    }
}
