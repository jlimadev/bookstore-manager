package com.jlima.bookstoremanager.controller

import org.springframework.http.ResponseEntity

interface BaseController<T> {
    fun create(entity: T): ResponseEntity<T>
}
