package com.jlima.bookstoremanager.providers.entity.domain

import com.jlima.bookstoremanager.providers.entity.AuditableEntity
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(schema = "domain", name = "author")
data class AuthorEntity(
    @Column(name = "name", length = 255)
    var name: String,

    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    var birthDate: Date,

    @Column(name = "is_active")
    var isActive: Boolean = true,
) : AuditableEntity()
