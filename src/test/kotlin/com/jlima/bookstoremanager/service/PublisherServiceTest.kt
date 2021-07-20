package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.PublisherDTO
import com.jlima.bookstoremanager.providers.entity.domain.PublisherEntity
import com.jlima.bookstoremanager.providers.entity.domain.toEntity
import com.jlima.bookstoremanager.providers.repository.PublisherRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.Date
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

    @Test
    fun findById() {
    }

    @Test
    fun findAll() {
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun deleteSoft() {
    }
}
