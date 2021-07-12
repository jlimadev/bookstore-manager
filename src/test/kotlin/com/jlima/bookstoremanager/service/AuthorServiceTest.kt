package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.domain.AuthorDTO
import com.jlima.bookstoremanager.providers.entity.domain.AuthorEntity
import com.jlima.bookstoremanager.providers.entity.domain.toEntity
import com.jlima.bookstoremanager.providers.repository.domain.AuthorRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.Date
import java.util.UUID
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
internal class AuthorServiceTest {
    private data class SUT(
        val sut: AuthorService,
        val authorRepository: AuthorRepository,
        val entityToCreate: AuthorDTO,
        val entityId: String
    )

    private fun makeSut(): SUT {
        val authorRepository: AuthorRepository = mock()
        val entityId = UUID.randomUUID().toString()
        val entityToCreate = AuthorDTO(
            name = "Jonathan Lima",
            birthDate = Date.from(Instant.now())
        )

        return SUT(
            sut = AuthorService(authorRepository),
            authorRepository = authorRepository,
            entityToCreate = entityToCreate,
            entityId = entityId
        )
    }

    @Test
    fun `It should create an author correctly and return a DTO`() {
        // Given [Arrange]
        val (sut, authorRepository, entityToCreate, entityId) = makeSut()
        val expectedCreatedEntity = entityToCreate.copy(id = entityId)

        // When [Act]
        whenever(authorRepository.save(entityToCreate.toEntity())).thenReturn(
            AuthorEntity(
                id = UUID.fromString(entityId),
                name = entityToCreate.name,
                birthDate = entityToCreate.birthDate,
                books = listOf()
            )
        )

        val response = sut.create(entityToCreate)

        // Then [Assert]
        assertEquals(expectedCreatedEntity, response)
    }
}
