package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.author.AuthorDTO
import com.jlima.bookstoremanager.dto.response.PaginationResponse
import com.jlima.bookstoremanager.dto.response.toPaginationResponse
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEmptyResponseException
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.providers.entity.domain.AuthorEntity
import com.jlima.bookstoremanager.providers.entity.domain.toDTO
import com.jlima.bookstoremanager.providers.entity.domain.toEntity
import com.jlima.bookstoremanager.providers.repository.AuthorRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthorService(
    private val authorRepository: AuthorRepository
) : CrudService<AuthorDTO> {

    override fun create(entity: AuthorDTO): AuthorDTO {
        val entityToInsert = entity.toEntity()
        val createdEntity = authorRepository.save(entityToInsert)
        return createdEntity.toDTO()
    }

    override fun findById(id: UUID): AuthorDTO {
        return findEntityById(id).toDTO()
    }

    override fun findAll(pageable: Pageable): PaginationResponse<AuthorDTO> {
        val databaseResult = authorRepository.findAll(pageable)
        val authorsList = databaseResult.toList()

        if (authorsList.isEmpty()) {
            throw BusinessEmptyResponseException(AvailableEntities.AUTHOR)
        }

        return databaseResult.toPaginationResponse(authorsList.map { it.toDTO() })
    }

    override fun update(id: UUID, entity: AuthorDTO): AuthorDTO {
        val existingAuthor = findEntityById(id)
        existingAuthor.name = entity.name
        existingAuthor.birthDate = entity.birthDate

        return authorRepository.save(existingAuthor).toDTO()
    }

    override fun delete(id: UUID): String {
        val existingAuthor = findEntityById(id)
        authorRepository.delete(existingAuthor)
        return "Success on deleting Author $id: ${existingAuthor.name}"
    }

    override fun deleteSoft(id: UUID): String {
        val existingAuthor = findEntityById(id)
        existingAuthor.isActive = false
        authorRepository.save(existingAuthor)
        return "Success on deleting Author $id: ${existingAuthor.name}"
    }

    private fun findEntityById(id: UUID): AuthorEntity {
        return authorRepository.findById(id)
            .orElseThrow { BusinessEntityNotFoundException(AvailableEntities.AUTHOR, id.toString()) }
    }
}
