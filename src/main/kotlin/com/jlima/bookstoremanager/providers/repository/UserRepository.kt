package com.jlima.bookstoremanager.providers.repository

import com.jlima.bookstoremanager.providers.entity.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByEmail(email: String): Optional<UserEntity>
}
