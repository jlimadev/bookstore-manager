package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.AuthorDTO
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEmptyResponseException
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.providers.entity.domain.AuthorEntity
import com.jlima.bookstoremanager.providers.entity.domain.toEntity
import com.jlima.bookstoremanager.providers.repository.AuthorRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.Date
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
@DisplayName("AuthorService Test Suit")
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

    @Nested
    @DisplayName("Create Tests")
    inner class Create {
        @Test
        fun `It should create an author correctly and return a DTO when call create with valid data`() {
            // Arrange
            val (sut, authorRepository, defaultDTO, defaultEntity, entityId) = makeSut()
            val expectedCreatedEntity = defaultDTO.copy(id = entityId.toString())

            // Act
            whenever(authorRepository.save(defaultDTO.toEntity())).thenReturn(defaultEntity)
            val response = sut.create(defaultDTO)

            // Assert
            assertEquals(expectedCreatedEntity, response)
            verify(authorRepository, times(1)).save(defaultDTO.toEntity())
        }
    }

    @Nested
    @DisplayName("findById Tests")
    inner class FindById {
        @Test
        fun `It should return a DTO from the found entity when call findById with existing id`() {
            // Arrange
            val (sut, authorRepository, defaultDTO, defaultEntity, entityId) = makeSut()
            val expectedFoundEntity = defaultDTO.copy(id = entityId.toString())

            // Act
            whenever(authorRepository.findById(entityId)).thenReturn(Optional.of(defaultEntity))
            val response = sut.findById(entityId)

            // Assert
            assertEquals(expectedFoundEntity, response)
            verify(authorRepository, times(1)).findById(entityId)
        }

        @Test
        fun `It should throw a EntityNotFoundException when call findById with non-existing id`() {
            // Arrange
            val (sut, authorRepository) = makeSut()
            val randomId = UUID.randomUUID()
            val expectedErrorMessage = "${AvailableEntities.AUTHOR} with id $randomId not found."

            // Act
            whenever(authorRepository.findById(randomId)).thenReturn(Optional.empty())

            // Assert
            val exception = assertThrows<BusinessEntityNotFoundException> {
                sut.findById(randomId)
            }

            assertEquals(expectedErrorMessage, exception.message)
            verify(authorRepository, times(1)).findById(randomId)
        }
    }

    @Nested
    @DisplayName("findAll Tests")
    inner class FindAll {
        @Test
        fun `It should return a list of authors when findAll is called`() {
            // Arrange
            val (sut, authorRepository, defaultDTO, defaultEntity, entityId) = makeSut()
            val expectedFoundEntities = listOf(defaultDTO.copy(id = entityId.toString()))

            // Then
            whenever(authorRepository.findAll()).thenReturn(listOf(defaultEntity))
            val response = sut.findAll()

            // Assert
            assertEquals(expectedFoundEntities, response)
            verify(authorRepository, times(1)).findAll()
        }

        @Test
        fun `It should throw BusinessEmptyResponseException when call findAll and it returns an empty response`() {
            // Arrange
            val (sut, authorRepository) = makeSut()
            val expectedErrorMessage = "No ${AvailableEntities.AUTHOR}(s) found."

            // Act
            whenever(authorRepository.findAll()).thenThrow(BusinessEmptyResponseException(AvailableEntities.AUTHOR))

            // Assert
            val exception = assertThrows<BusinessEmptyResponseException> { sut.findAll() }
            assertEquals(expectedErrorMessage, exception.message)
            verify(authorRepository, times(1)).findAll()
        }
    }
}
