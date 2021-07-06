package com.jlima.bookstoremanager.providers.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.Date
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditableEntity : BaseEntity() {
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    protected lateinit var createdBy: String
        private set

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected lateinit var createdAt: Date
        private set

    @LastModifiedBy
    @Column(name = "updated_by", updatable = true)
    protected lateinit var updatedBy: String
        private set

    @LastModifiedDate
    @Column(name = "updated_at", updatable = true)
    protected lateinit var updatedAt: Date
        private set
}
