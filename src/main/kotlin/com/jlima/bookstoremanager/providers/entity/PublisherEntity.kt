package com.jlima.bookstoremanager.providers.entity

import java.util.Date
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(schema = "domain", name = "publisher")
data class PublisherEntity(
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "name")
    val name: String,

    @Column(name = "code")
    val code: String,

    @Column(name = "foundation_date")
    @Temporal(TemporalType.TIMESTAMP)
    val foundationDate: Date,

    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
    val books: List<BookEntity>
)
