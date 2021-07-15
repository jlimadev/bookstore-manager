package com.jlima.bookstoremanager.exception.model

import java.util.UUID
import javax.persistence.EntityNotFoundException

class AuthorNotFoundException(val id: UUID) : EntityNotFoundException("Author with id $id not found. Please try again.")
