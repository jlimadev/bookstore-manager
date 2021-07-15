package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.AuthorDTO
import com.jlima.bookstoremanager.exception.model.AuthorNotFoundException
import com.jlima.bookstoremanager.providers.entity.domain.AuthorEntity
import com.jlima.bookstoremanager.providers.entity.domain.toEntity
import com.jlima.bookstoremanager.providers.repository.AuthorRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.Date
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
internal class AuthorServiceTest {
    private data class SUT(
        val sut: AuthorService,
        val authorRepository: AuthorRepository,
        val defaultDTO: AuthorDTO,
        val defaultEntity: AuthorEntity,
        val entityId: UUID
    )

    private fun makeSut(): SUT {
        val authorRepository: AuthorRepository = mock()
        val entityId = UUID.randomUUID()

        val defaultDTO = AuthorDTO(
            name = "Jonathan Lima",
            birthDate = Date.from(Instant.now())
        )

        val defaultEntity = AuthorEntity(
            id = entityId,
            name = defaultDTO.name,
            birthDate = defaultDTO.birthDate
        )

        return SUT(
            sut = AuthorService(authorRepository),
            authorRepository = authorRepository,
            defaultDTO = defaultDTO,
            defaultEntity = defaultEntity,
            entityId = entityId
        )
    }

    @Test
    fun `It should create an author correctly and return a DTO`() {
        // Given [Arrange]
        val (sut, authorRepository, defaultDTO, defaultEntity, entityId) = makeSut()
        val expectedCreatedEntity = defaultDTO.copy(id = entityId.toString())

        // When [Act]
        whenever(authorRepository.save(defaultDTO.toEntity())).thenReturn(defaultEntity)
        val response = sut.create(defaultDTO)

        // Then [Assert]
        assertEquals(expectedCreatedEntity, response)
    }

    @Test
    fun `It should findById`() {
        // Given [Arrange]
        val (sut, authorRepository, defaultDTO, defaultEntity, entityId) = makeSut()
        val expectedFoundEntity = defaultDTO.copy(id = entityId.toString())

        // When [Act]
        whenever(authorRepository.findById(entityId)).thenReturn(Optional.of(defaultEntity))
        val response = sut.findById(entityId)

        // Then [Assert]
        assertEquals(expectedFoundEntity, response)
    }

    @Test
    fun `It should throw a EntityNotFoundException when cannot findById`() {
        // Given [Arrange]
        val (sut, authorRepository) = makeSut()
        val randomId = UUID.randomUUID()
        val expectedErrorMessage = "Author with id $randomId not found. Please try again."

        // When [Act]
        whenever(authorRepository.findById(randomId)).thenReturn(Optional.empty())

        // Then [Assert]
        assertThrows<AuthorNotFoundException>(message = expectedErrorMessage) {
            sut.findById(randomId)
        }
    }

    @Test
    fun `It should return a list of authors when findAll is called`() {
        // Given [Arrange]
        val (sut, authorRepository, defaultDTO, defaultEntity, entityId) = makeSut()
        val expectedFoundEntities = listOf(defaultDTO.copy(id = entityId.toString()))

        // When [Then]
        whenever(authorRepository.findAll()).thenReturn(listOf(defaultEntity))
        val response = sut.findAll()

        // Then [Assert]
        assertEquals(expectedFoundEntities, response)
    }
}
