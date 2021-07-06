package com.jlima.bookstoremanager.providers.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.Date
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditableEntity {

    @CreatedBy
    protected lateinit var createdBy: String
        private set

    @CreatedDate
    protected lateinit var createdAt: Date
        private set

    @LastModifiedBy
    protected lateinit var updatedBy: String
        private set

    @LastModifiedDate
    protected lateinit var updatedAt: Date
        private set
}
