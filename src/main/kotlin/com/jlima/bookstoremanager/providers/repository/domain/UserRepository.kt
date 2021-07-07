package com.jlima.bookstoremanager.providers.repository.domain

import com.jlima.bookstoremanager.providers.entity.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID>
