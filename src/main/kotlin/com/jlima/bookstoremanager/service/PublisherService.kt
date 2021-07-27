package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.PublisherDTO
import com.jlima.bookstoremanager.dto.response.PaginationResponse
import com.jlima.bookstoremanager.dto.response.toPaginationResponse
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEmptyResponseException
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.providers.entity.domain.PublisherEntity
import com.jlima.bookstoremanager.providers.entity.domain.toDTO
import com.jlima.bookstoremanager.providers.entity.domain.toEntity
import com.jlima.bookstoremanager.providers.repository.PublisherRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PublisherService(
    private val publisherRepository: PublisherRepository
) : CrudService<PublisherDTO> {
    override fun create(entity: PublisherDTO): PublisherDTO {
        return publisherRepository.save(entity.toEntity()).toDTO()
    }

    override fun findById(id: UUID): PublisherDTO {
        return findEntityById(id).toDTO()
    }

    override fun findAll(pageable: Pageable): PaginationResponse<PublisherDTO> {
        val databaseResult = publisherRepository.findAll(pageable)
        val foundPublishers = databaseResult.toList()

        if (foundPublishers.isEmpty()) {
            throw BusinessEmptyResponseException(AvailableEntities.PUBLISHER)
        }

        return databaseResult.toPaginationResponse(foundPublishers.map { it.toDTO() })
    }

    override fun update(id: UUID, entity: PublisherDTO): PublisherDTO {
        val foundPublisher = findEntityById(id)
        foundPublisher.name = entity.name
        foundPublisher.code = entity.code
        foundPublisher.foundationDate = entity.foundationDate
        return publisherRepository.save(foundPublisher).toDTO()
    }

    override fun delete(id: UUID): String {
        val foundPublisher = findEntityById(id)
        publisherRepository.delete(foundPublisher)
        return "Success on deleting Publisher $id: ${foundPublisher.name}"
    }

    override fun deleteSoft(id: UUID): String {
        val foundPublisher = findEntityById(id)
        foundPublisher.isActive = false
        publisherRepository.save(foundPublisher)
        return "Success on (soft) deleting Publisher $id: ${foundPublisher.name}"
    }

    private fun findEntityById(id: UUID): PublisherEntity {
        return publisherRepository.findById(id)
            .orElseThrow { BusinessEntityNotFoundException(AvailableEntities.PUBLISHER, id.toString()) }
    }
}
