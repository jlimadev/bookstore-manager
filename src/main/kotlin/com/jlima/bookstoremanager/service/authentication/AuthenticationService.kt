package com.jlima.bookstoremanager.service.authentication

import com.jlima.bookstoremanager.dto.AuthenticatedUser
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.providers.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository
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
}
