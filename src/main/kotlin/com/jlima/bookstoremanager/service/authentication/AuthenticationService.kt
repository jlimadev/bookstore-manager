package com.jlima.bookstoremanager.service.authentication

import com.jlima.bookstoremanager.dto.auth.AuthRequest
import com.jlima.bookstoremanager.dto.auth.AuthResponse
import com.jlima.bookstoremanager.dto.auth.AuthenticatedUser
import com.jlima.bookstoremanager.exception.model.BusinessAuthenticationException
import com.jlima.bookstoremanager.providers.entity.domain.UserEntity
import com.jlima.bookstoremanager.providers.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = getUserByUsername(username)
        return AuthenticatedUser(
            username = user.email,
            password = user.password,
            role = user.role.name
        )
    }

    fun createJwtToken(authRequest: AuthRequest): AuthResponse {
        authenticate(authRequest.username, authRequest.password)
        val userDetails = loadUserByUsername(authRequest.username)
        val token = jwtTokenProvider.generateToken(userDetails)

        return AuthResponse(
            accessToken = token
        )
    }

    private fun authenticate(username: String, password: String) {
        getUserByUsername(username)
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
    }

    private fun getUserByUsername(username: String): UserEntity {
        return userRepository.findByEmail(username)
            .orElseThrow { BusinessAuthenticationException("Invalid username or Password") }
    }
}
