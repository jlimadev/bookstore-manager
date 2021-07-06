package com.jlima.bookstoremanager.providers.entity.domain

import com.jlima.bookstoremanager.providers.entity.AuditableEntity
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(schema = "domain", name = "publisher")
data class PublisherEntity(
    @Column(name = "name")
    var name: String,

    @Column(name = "code")
    var code: String,

    @Column(name = "foundation_date")
    @Temporal(TemporalType.TIMESTAMP)
    var foundationDate: Date,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
    val books: List<BookEntity>,
) : AuditableEntity()
