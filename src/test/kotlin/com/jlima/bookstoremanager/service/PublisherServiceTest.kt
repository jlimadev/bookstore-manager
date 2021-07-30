package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.publisher.PublisherDTO
import com.jlima.bookstoremanager.dto.response.PaginationResponse
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEmptyResponseException
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.providers.entity.domain.PublisherEntity
import com.jlima.bookstoremanager.providers.entity.domain.toEntity
import com.jlima.bookstoremanager.providers.repository.PublisherRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.Instant
import java.util.Date
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals

internal class PublisherServiceTest {

    private data class SUT(
        val sut: PublisherService,
        val publisherRepository: PublisherRepository,
        val defaultDTO: PublisherDTO,
        val defaultEntity: PublisherEntity,
        val entityId: UUID
    )

    private fun makeSut(): SUT {
        val publisherRepository: PublisherRepository = mock()
        val sut = PublisherService(publisherRepository)
        val entityId = UUID.randomUUID()

        val defaultDTO = PublisherDTO(
            name = "Any publisher",
            code = "AnyCode",
            foundationDate = Date.from(Instant.now())
        )

        val defaultEntity = PublisherEntity(
            id = entityId,
            name = defaultDTO.name,
            code = defaultDTO.code,
            foundationDate = defaultDTO.foundationDate
        )

        return SUT(
            sut = sut,
            publisherRepository = publisherRepository,
            defaultDTO = defaultDTO,
            defaultEntity = defaultEntity,
            entityId = entityId
        )
    }

    @Nested
    @DisplayName("Create")
    inner class Create {
        @Test
        fun `It should create a Publisher correctly and return a DTO when call create with valid data`() {
            // Arrange
            val (sut, publisherRepository, defaultDTO, defaultEntity, entityId) = makeSut()
            val expectedResponse = defaultDTO.copy(id = entityId.toString())
            whenever(publisherRepository.save(defaultDTO.toEntity())).thenReturn(defaultEntity)

            // Act
            val response = sut.create(defaultDTO)

            // Assert
            assertEquals(expectedResponse, response)
            verify(publisherRepository, times(1)).save(defaultDTO.toEntity())
        }
    }

    @Nested
    @DisplayName("FindById")
    inner class FindById {
        @Test
        fun `It should return a DTO from the found entity when call findById with existing id`() {
            // Arrange
            val (sut, publisherRepository, defaultDTO, defaultEntity, entityId) = makeSut()
            val expectedResponse = defaultDTO.copy(id = entityId.toString())
            whenever(publisherRepository.findById(entityId)).thenReturn(Optional.of(defaultEntity))

            // Act
            val response = sut.findById(entityId)

            // Assert
            assertEquals(expectedResponse, response)
            verify(publisherRepository, times(1)).findById(entityId)
        }

        @Test
        fun `It should throw a EntityNotFoundException when call findById with non-existing id`() {
            // Arrange
            val (sut, publisherRepository) = makeSut()
            val randomId = UUID.randomUUID()
            val expectedErrorMessage = "${AvailableEntities.PUBLISHER} $randomId not found."
            whenever(publisherRepository.findById(randomId)).thenReturn(Optional.empty())

            // Act
            val exception = assertThrows<BusinessEntityNotFoundException> { sut.findById(randomId) }

            // Assert
            assertEquals(expectedErrorMessage, exception.message)
            verify(publisherRepository, times(1)).findById(randomId)
        }
    }

    @Nested
    @DisplayName("FindAll")
    inner class FindAll {
        @Test
        fun `It should return a list of publisher when findAll is called`() {
            // Arrange
            val (sut, publisherRepository, defaultDTO, defaultEntity, entityId) = makeSut()
            val expectedFoundEntities = listOf(defaultDTO.copy(id = entityId.toString()))
            val pageable = PageRequest.of(0, 5, Sort.by("any").ascending())
            val expectedPaginationResponse = PaginationResponse(
                totalPages = 1,
                totalItems = 1,
                currentPage = 0,
                currentItems = 1,
                data = expectedFoundEntities
            )

            whenever(publisherRepository.findAll(pageable)).thenReturn((PageImpl(listOf(defaultEntity))))

            // Act
            val response = sut.findAll(pageable)

            // Assert
            assertEquals(expectedPaginationResponse, response)
            verify(publisherRepository, times(1)).findAll(pageable)
        }

        @Test
        fun `It should throw BusinessEmptyResponseException when call findAll and it returns an empty response`() {
            // Arrange
            val (sut, publisherRepository) = makeSut()
            val expectedErrorMessage = "No ${AvailableEntities.PUBLISHER}(s) found."
            val pageable = PageRequest.of(1, 1, Sort.by("any"))
            whenever(publisherRepository.findAll(pageable)).thenReturn(Page.empty())

            // Act
            val exception = assertThrows<BusinessEmptyResponseException> { sut.findAll(pageable) }

            // Assert
            assertEquals(expectedErrorMessage, exception.message)
            verify(publisherRepository, times(1)).findAll(pageable)
        }
    }

    @Nested
    @DisplayName("Update")
    inner class Update {
        @Test
        fun `It should update successfully`() {
            // Arrange
            val (sut, publisherRepository, defaultDTO, defaultEntity, entityId) = makeSut()
            val updateDataDTO = defaultDTO.copy(name = "Updated name", code = "Updated Code")
            val updatedEntity = defaultEntity.copy(name = updateDataDTO.name, code = updateDataDTO.code)
            val expectedResponse = updateDataDTO.copy(id = entityId.toString())
            whenever(publisherRepository.findById(entityId)).thenReturn(Optional.of(defaultEntity))
            whenever(publisherRepository.save(updatedEntity)).thenReturn(updatedEntity)

            // Act
            val response = sut.update(entityId, updateDataDTO)

            // Assert
            assertEquals(expectedResponse, response)
            verify(publisherRepository, times(1)).findById(entityId)
            verify(publisherRepository, times(1)).save(updatedEntity)
        }

        @Test
        fun `It should throw BusinessNotFoundEntityException when call update to non-existing entity`() {
            // Arrange
            val (sut, publisherRepository, defaultDTO) = makeSut()
            val nonExistingId = UUID.randomUUID()
            val expectedErrorMessage = "${AvailableEntities.PUBLISHER} $nonExistingId not found."
            whenever(publisherRepository.findById(nonExistingId)).thenReturn(Optional.empty())

            // Act
            val exception = assertThrows<BusinessEntityNotFoundException> { sut.update(nonExistingId, defaultDTO) }

            // Assert
            assertEquals(expectedErrorMessage, exception.message)
            verify(publisherRepository, times(1)).findById(nonExistingId)
            verify(publisherRepository, never()).save(any())
        }
    }

    @Nested
    @DisplayName("Delete")
    inner class Delete {
        @Test
        fun `It should delete successfully`() {
            // Arrange
            val (sut, publisherRepository, _, defaultEntity, entityId) = makeSut()
            whenever(publisherRepository.findById(entityId)).thenReturn(Optional.of(defaultEntity))
            val expectedResponse = "Success on deleting Publisher $entityId: ${defaultEntity.name}"

            // Act
            val response = sut.delete(entityId)

            // Assert
            assertEquals(expectedResponse, response)
            verify(publisherRepository, times(1)).findById(entityId)
            verify(publisherRepository, times(1)).delete(defaultEntity)
        }

        @Test
        fun `It should throw BusinessNotFoundEntityException when call delete to non-existing entity`() {
            // Arrange
            val (sut, publisherRepository) = makeSut()
            val nonExistingId = UUID.randomUUID()
            whenever(publisherRepository.findById(nonExistingId)).thenReturn(Optional.empty())

            // Assert
            assertThrows<BusinessEntityNotFoundException> { sut.delete(nonExistingId) }
            verify(publisherRepository, times(1)).findById(nonExistingId)
            verify(publisherRepository, never()).delete(any())
        }
    }

    @Nested
    @DisplayName("Delete (Soft)")
    inner class DeleteSoft {
        @Test
        fun `It should update isActive to false when call deleteSoft method`() {
            // Arrange
            val (sut, publisherRepository, _, defaultEntity, entityId) = makeSut()
            val expectedDeletedEntity = defaultEntity.copy(isActive = false)
            val expectedResponse = "Success on (soft) deleting Publisher $entityId: ${defaultEntity.name}"

            whenever(publisherRepository.findById(entityId)).thenReturn(Optional.of(defaultEntity))

            // Act
            val response = sut.deleteSoft(entityId)

            // Assert
            assertEquals(expectedResponse, response)
            verify(publisherRepository, times(1)).save(expectedDeletedEntity)
            verify(publisherRepository, never()).delete(any())
        }

        @Test
        fun `It should throw BusinessNotFoundEntityException when call deleteSoft to non-existing entity`() {
            // Arrange
            val (sut, publisherRepository) = makeSut()
            val nonExistingId = UUID.randomUUID()
            whenever(publisherRepository.findById(nonExistingId)).thenReturn(Optional.empty())

            // Assert
            assertThrows<BusinessEntityNotFoundException> { sut.deleteSoft(nonExistingId) }
            verify(publisherRepository, times(1)).findById(nonExistingId)
            verify(publisherRepository, never()).save(any())
        }
    }
}
