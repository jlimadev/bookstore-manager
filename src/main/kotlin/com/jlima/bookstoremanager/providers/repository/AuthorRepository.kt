package com.jlima.bookstoremanager.providers.repository

import com.jlima.bookstoremanager.providers.entity.domain.AuthorEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AuthorRepository : JpaRepository<AuthorEntity, UUID>
