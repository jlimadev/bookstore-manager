package com.jlima.bookstoremanager.config.audit

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
class AuditConfig {
    @Bean
    fun auditorAware(): AuditorAware<String> {
        return CustomAuditAware()
    }
}
