package com.jlima.bookstoremanager.service

interface CrudService<T> {
    fun create(entity: T): T
}
