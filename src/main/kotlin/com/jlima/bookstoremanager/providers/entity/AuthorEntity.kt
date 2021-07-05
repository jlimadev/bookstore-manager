package com.jlima.bookstoremanager.providers.entity

import java.util.Date
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(schema = "domain", name = "author")
data class AuthorEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "name", length = 255)
    val name: String,

    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    val birthDate: Date,

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    val books: List<BookEntity>
)
