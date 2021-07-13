package com.jlima.bookstoremanager.providers.repository

import com.jlima.bookstoremanager.providers.entity.domain.BookEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BookRepository : JpaRepository<BookEntity, UUID>
