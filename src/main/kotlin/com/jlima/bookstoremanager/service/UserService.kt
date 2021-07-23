package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.UserDTO
import com.jlima.bookstoremanager.dto.response.PaginationResponse
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEntityExistsException
import com.jlima.bookstoremanager.providers.entity.domain.toDTO
import com.jlima.bookstoremanager.providers.entity.domain.toEntity
import com.jlima.bookstoremanager.providers.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) : CrudService<UserDTO> {
    override fun create(entity: UserDTO): UserDTO {
        checkEmailExists(entity.email)
        return userRepository.save(entity.toEntity()).toDTO()
    }

    override fun findById(id: UUID): UserDTO {
        TODO("Not yet implemented")
    }

    override fun findAll(pageable: Pageable): PaginationResponse<UserDTO> {
        TODO("Not yet implemented")
    }

    override fun update(id: UUID, entity: UserDTO): UserDTO {
        TODO("Not yet implemented")
    }

    override fun delete(id: UUID): String {
        TODO("Not yet implemented")
    }

    override fun deleteSoft(id: UUID): String {
        TODO("Not yet implemented")
    }

    private fun checkEmailExists(email: String) {
        userRepository.findByEmail(email).ifPresent {
            throw BusinessEntityExistsException(AvailableEntities.USER, email)
        }
    }
}
