package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.providers.repository.domain.AuthorRepository
import org.springframework.stereotype.Service

@Service
class AuthorService(
    private val authorRepository: AuthorRepository
)
