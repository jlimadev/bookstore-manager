package com.jlima.bookstoremanager.service

import java.util.UUID

interface CrudService<T> {
    fun create(entity: T): T
    fun findById(id: UUID): T
    fun findAll(): List<T>
}