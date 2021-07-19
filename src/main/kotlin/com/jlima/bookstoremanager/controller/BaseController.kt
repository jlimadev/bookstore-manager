package com.jlima.bookstoremanager.controller

import com.jlima.bookstoremanager.dto.response.CustomMessageResponse
import org.springframework.http.ResponseEntity
import java.util.UUID

interface BaseController<T> {
    fun create(entity: T): ResponseEntity<T>
    fun findById(id: UUID): ResponseEntity<T>
    fun findAll(): ResponseEntity<List<T>>
    fun update(id: UUID, body: T): ResponseEntity<T>
    fun delete(id: UUID): ResponseEntity<CustomMessageResponse>
    fun hardDelete(id: UUID): ResponseEntity<CustomMessageResponse>
}
