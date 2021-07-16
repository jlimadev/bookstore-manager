package com.jlima.bookstoremanager.controller

import org.springframework.http.ResponseEntity
import java.util.UUID

interface BaseController<T> {
    fun create(entity: T): ResponseEntity<T>
    fun findById(id: UUID): ResponseEntity<T>
    fun findAll(): ResponseEntity<List<T>>
}
