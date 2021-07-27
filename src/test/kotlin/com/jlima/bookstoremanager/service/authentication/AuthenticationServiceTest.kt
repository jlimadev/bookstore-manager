package com.jlima.bookstoremanager.service.authentication

import com.jlima.bookstoremanager.enums.Gender
import com.jlima.bookstoremanager.enums.Role
import com.jlima.bookstoremanager.providers.entity.domain.UserEntity
import com.jlima.bookstoremanager.providers.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.Instant
import java.time.Period
import java.util.Date
import java.util.Optional
import java.util.UUID

internal class AuthenticationServiceTest {
    private val userRepository: UserRepository = mock()
    private val authenticationService = AuthenticationService(userRepository)

    @Test
    fun `It should return a UserDetails when email exists`() {
        // Arrange
        val userEntity = UserEntity(
            id = UUID.randomUUID(),
            name = "Any Name",
            gender = Gender.MALE,
            birthDate = Date.from(Instant.now().minus(Period.ofDays(5000))),
            email = "any@mail.com",
            password = "myStr0ngP@ss",
            role = Role.USER
        )

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
}
