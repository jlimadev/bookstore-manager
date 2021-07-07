package com.jlima.bookstoremanager.providers.repository.domain

import com.jlima.bookstoremanager.providers.entity.domain.PublisherEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PublisherRepository : JpaRepository<PublisherEntity, UUID>
