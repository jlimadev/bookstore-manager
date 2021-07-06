package com.jlima.bookstoremanager.providers.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: UUID? = null
}
