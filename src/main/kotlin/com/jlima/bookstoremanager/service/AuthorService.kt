package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.domain.AuthorDTO
import com.jlima.bookstoremanager.providers.entity.domain.toDTO
import com.jlima.bookstoremanager.providers.entity.domain.toEntity
import com.jlima.bookstoremanager.providers.repository.domain.AuthorRepository
import org.springframework.stereotype.Service

@Service
class AuthorService(
    private val authorRepository: AuthorRepository
) : CrudService<AuthorDTO> {

    override fun create(entity: AuthorDTO): AuthorDTO {
        val entityToInsert = entity.toEntity()
        val createdEntity = authorRepository.save(entityToInsert)
        return createdEntity.toDTO()
    }
}
