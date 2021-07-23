package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.UserDTO
import com.jlima.bookstoremanager.dto.response.PaginationResponse
import com.jlima.bookstoremanager.enums.Gender
import com.jlima.bookstoremanager.enums.Role
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEmptyResponseException
import com.jlima.bookstoremanager.exception.model.BusinessEntityExistsException
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.providers.entity.domain.UserEntity
import com.jlima.bookstoremanager.providers.entity.domain.toDTO
import com.jlima.bookstoremanager.providers.entity.domain.toEntity
import com.jlima.bookstoremanager.providers.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
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
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.time.Instant
import java.time.Period
import java.util.Date
import java.util.Optional
import java.util.UUID

internal class UserServiceTest {
    private data class SUT(
        val sut: UserService,
        val userRepository: UserRepository,
        val userDTO: UserDTO,
        val userEntity: UserEntity,
        val userId: UUID
    )

    private fun makeSut(): SUT {
        val userRepository: UserRepository = mock()
        val userService = UserService(userRepository)
        val userId = UUID.randomUUID()
        val userDTO = UserDTO(
            name = "Any Name",
            gender = Gender.MALE,
            birthDate = Date.from(Instant.now().minus(Period.ofDays(5000))),
            email = "any@mail.com",
            password = "myStr0ngP@ss",
            role = Role.ADMIN
        )

        val userEntity = userDTO.toEntity().copy(id = userId)

        return SUT(
            sut = userService,
            userRepository = userRepository,
            userDTO = userDTO,
            userEntity = userEntity,
            userId = userId
        )
    }

    @Nested
    @DisplayName("Create")
    inner class Create {
        @Test
        fun `It should create successfully`() {
            // Arrange
            val (sut, userRepository, userDTO, userEntity) = makeSut()
            val expectedResult = userEntity.toDTO()
            whenever(userRepository.findByEmail(userDTO.email)).thenReturn(Optional.empty())
            whenever(userRepository.save(userDTO.toEntity())).thenReturn(userEntity)

            // Act
            val result = sut.create(userDTO)

            // Assert
            assertEquals(expectedResult, result)
            verify(userRepository, times(1)).findByEmail(userDTO.email)
            verify(userRepository, times(1)).save(userDTO.toEntity())
        }

        @Test
        fun `It should throw BusinessEntityExistsException when create user with already registered email`() {
            // Arrange
            val (sut, userRepository, userDTO, userEntity) = makeSut()
            val expectedErrorMessage = "Entity ${AvailableEntities.USER} already exists: ${userDTO.email}"
            whenever(userRepository.findByEmail(userDTO.email)).thenReturn(Optional.of(userEntity))

            // Act
            val exception = assertThrows<BusinessEntityExistsException> { sut.create(userDTO) }

            // Assert
            assertEquals(expectedErrorMessage, exception.message)
            verify(userRepository, times(1)).findByEmail(userDTO.email)
            verify(userRepository, never()).save(any())
        }
    }

    @Nested
    @DisplayName("FindById")
    inner class FindById {
        @Test
        fun `It should findById`() {
            // Arrange
            val (sut, userRepository, _, userEntity, userId) = makeSut()
            val expectedResult = userEntity.toDTO()
            whenever(userRepository.findById(userId)).thenReturn(Optional.of(userEntity))

            // Act
            val result = sut.findById(userId)

            // Assert
            assertEquals(expectedResult, result)
            verify(userRepository, times(1)).findById(userId)
        }

        @Test
        fun `It should throw BusinessEntityExistsException when id is not found`() {
            // Arrange
            val (sut, userRepository, _, _, userId) = makeSut()
            val expectedError = "${AvailableEntities.USER} with id $userId not found."
            whenever(userRepository.findById(userId)).thenReturn(Optional.empty())

            // Act
            val exception = assertThrows<BusinessEntityNotFoundException> { sut.findById(userId) }

            // Assert
            assertEquals(expectedError, exception.message)
            verify(userRepository, times(1)).findById(userId)
        }
    }

    @Nested
    @DisplayName("FindAll")
    inner class FindAll {
        @Test
        fun `It should findAll and return a pagination response`() {
            // Arrange
            val (sut, userRepository, _, userEntity) = makeSut()
            val pageable = PageRequest.of(0, 10, Sort.by("any").ascending())
            val expectedListOfUsers = listOf(userEntity.toDTO())
            val expectedResult = PaginationResponse(
                totalPages = 1,
                totalItems = 1,
                currentPage = 0,
                currentItems = 1,
                data = expectedListOfUsers
            )
            whenever(userRepository.findAll(pageable)).thenReturn(PageImpl(listOf(userEntity)))

            // Act
            val result = sut.findAll(pageable)

            // Assert
            assertEquals(expectedResult, result)
            verify(userRepository, times(1)).findAll(pageable)
        }

        @Test
        fun `It should throw BusinessEmptyResponseException when call findAll and it returns an empty response`() {
            // Arrange
            val (sut, userRepository, _, userEntity) = makeSut()
            val pageable: Pageable = mock()
            val expectedErrorMessage = "No ${AvailableEntities.USER}(s) found."
            whenever(userRepository.findAll(pageable)).thenReturn(Page.empty())

            // Act
            val exception = assertThrows<BusinessEmptyResponseException> { sut.findAll(pageable) }

            // Assert
            assertEquals(expectedErrorMessage, exception.message)
        }
    }
}