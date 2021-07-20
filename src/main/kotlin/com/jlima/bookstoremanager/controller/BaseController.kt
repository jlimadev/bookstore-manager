package com.jlima.bookstoremanager.controller

import com.jlima.bookstoremanager.dto.response.CustomMessageResponse
import com.jlima.bookstoremanager.dto.response.PaginationResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import java.util.UUID

interface BaseController<T> {
    fun create(entity: T): ResponseEntity<T>
    fun findById(id: UUID): ResponseEntity<T>
    fun findAll(pageable: Pageable): ResponseEntity<PaginationResponse<T>>
    fun update(id: UUID, body: T): ResponseEntity<T>
    fun delete(id: UUID): ResponseEntity<CustomMessageResponse>
    fun deleteSoft(id: UUID): ResponseEntity<CustomMessageResponse>
}
