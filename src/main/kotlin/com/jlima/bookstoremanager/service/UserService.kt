package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.response.PaginationResponse
import com.jlima.bookstoremanager.dto.response.toPaginationResponse
import com.jlima.bookstoremanager.dto.user.UserDTO
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEmptyResponseException
import com.jlima.bookstoremanager.exception.model.BusinessEntityExistsException
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.providers.entity.domain.UserEntity
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

        val userToBeCreated = entity.toEntity()

        return userRepository.save(userToBeCreated).toDTO()
    }

    override fun findById(id: UUID): UserDTO {
        return findEntityById(id).toDTO()
    }

    override fun findAll(pageable: Pageable): PaginationResponse<UserDTO> {
        val databaseResult = userRepository.findAll(pageable)
        val users = databaseResult.toList()

        if (users.isEmpty()) {
            throw BusinessEmptyResponseException(AvailableEntities.USER)
        }

        return databaseResult.toPaginationResponse(users.map { it.toDTO() })
    }

    override fun update(id: UUID, entity: UserDTO): UserDTO {
        val user = findEntityById(id)

        user.name = entity.name
        user.gender = entity.gender
        user.birthDate = entity.birthDate
        user.email = entity.email
        user.password = entity.password
        user.role = entity.role

        return userRepository.save(user).toDTO()
    }

    override fun delete(id: UUID): String {
        val user = findEntityById(id)
        userRepository.delete(user)
        return "Success on deleting User $id: ${user.name}"
    }

    override fun deleteSoft(id: UUID): String {
        val user = findEntityById(id)
        user.isActive = false
        userRepository.save(user)
        return "Success on (soft) deleting User $id: ${user.name}"
    }

    private fun findEntityById(id: UUID): UserEntity {
        return userRepository.findById(id)
            .orElseThrow { BusinessEntityNotFoundException(AvailableEntities.USER, id.toString()) }
    }

    private fun checkEmailExists(email: String) {
        userRepository.findByEmail(email).ifPresent {
            throw BusinessEntityExistsException(AvailableEntities.USER, email)
        }
    }
}
