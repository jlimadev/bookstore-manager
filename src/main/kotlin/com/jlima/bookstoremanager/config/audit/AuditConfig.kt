package com.jlima.bookstoremanager.config.audit

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.Optional

@Configuration
@EnableJpaAuditing
class AuditConfig {
    @Bean
    fun auditorAware(): AuditorAware<String> {
        return CustomAuditAware()
    }
}

class CustomAuditAware : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of("Uzumaki Naruto").filter { it.isNotEmpty() }
    }
}
