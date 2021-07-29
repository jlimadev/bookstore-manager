package com.jlima.bookstoremanager.service.authentication

import com.jlima.bookstoremanager.dto.AuthenticatedUser
import com.jlima.bookstoremanager.dto.response.AuthRequest
import com.jlima.bookstoremanager.dto.response.AuthResponse
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
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
        val user = userRepository.findByEmail(username)
            .orElseThrow { BusinessEntityNotFoundException(AvailableEntities.USER, username) }
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
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
    }
}
