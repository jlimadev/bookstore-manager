package com.jlima.bookstoremanager.service.authentication

import com.jlima.bookstoremanager.dto.auth.AuthRequest
import com.jlima.bookstoremanager.dto.auth.AuthResponse
import com.jlima.bookstoremanager.enums.Gender
import com.jlima.bookstoremanager.enums.Role
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.providers.entity.domain.UserEntity
import com.jlima.bookstoremanager.providers.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant
import java.time.Period
import java.util.Date
import java.util.Optional
import java.util.UUID

internal class AuthenticationServiceTest {
    private val userRepository: UserRepository = mock()
    private val authenticationManager: AuthenticationManager = mock()
    private val jwtTokenProvider: JwtTokenProvider = mock()
    private val authenticationService =
        AuthenticationService(userRepository, authenticationManager, jwtTokenProvider)

    private val userEntity = UserEntity(
        id = UUID.randomUUID(),
        name = "Any Name",
        gender = Gender.MALE,
        birthDate = Date.from(Instant.now().minus(Period.ofDays(5000))),
        email = "any@mail.com",
        password = "myStr0ngP@ss",
        role = Role.USER
    )

    private fun <T> anyClassOf(type: Class<T>): T = Mockito.any(type)

    @Nested
    inner class LoadByUsername {
        @Test
        fun `It should return a UserDetails when email exists`() {
            // Arrange
            val expectedUsername = userEntity.email
            val expectedPassword = userEntity.password
            val expectedRole = SimpleGrantedAuthority("ROLE_${userEntity.role}")

            whenever(userRepository.findByEmail(userEntity.email)).thenReturn(Optional.of(userEntity))

            // Act
            val result = authenticationService.loadUserByUsername(userEntity.email)

            // Assert
            assertEquals(expectedUsername, result.username)
            assertEquals(expectedPassword, result.password)
            assertTrue(result.authorities.contains(expectedRole))
        }

        @Test
        fun `It should throw BusinessEntityNotFoundException when username is not found`() {
            // Arrange
            val invalidEmail = "invalid@narnia.com"
            whenever(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty())

            // Act
            assertThrows<BusinessEntityNotFoundException> { authenticationService.loadUserByUsername(invalidEmail) }

            // Assert
            verify(userRepository, times(1)).findByEmail(invalidEmail)
        }
    }

    @Nested
    inner class CreateJwtToken {
        @Test
        fun `It should create a jwt token`() {
            // Arrange
            val authRequest = AuthRequest(userEntity.email, userEntity.password)
            val expectedToken = AuthResponse(accessToken = "myApplicationToken")

            whenever(jwtTokenProvider.generateToken(anyClassOf(UserDetails::class.java))).thenReturn(expectedToken.accessToken)
            whenever(userRepository.findByEmail(authRequest.username)).thenReturn(Optional.of(userEntity))

            // Act
            val token = authenticationService.createJwtToken(authRequest)

            // Assert
            assertEquals(expectedToken, token)
        }
    }
}
