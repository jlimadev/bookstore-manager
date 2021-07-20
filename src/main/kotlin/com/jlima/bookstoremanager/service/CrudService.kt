package com.jlima.bookstoremanager.service

import com.jlima.bookstoremanager.dto.response.PaginationResponse
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CrudService<T> {
    fun create(entity: T): T
    fun findById(id: UUID): T
    fun findAll(pageable: Pageable): PaginationResponse<T>
    fun update(id: UUID, entity: T): T
    fun delete(id: UUID): String
    fun deleteSoft(id: UUID): String
}
