package com.jlima.bookstoremanager.providers.entity.domain

import com.jlima.bookstoremanager.providers.entity.AuditableEntity
import java.util.Date
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "book", schema = "domain")
data class BookEntity(
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "name")
    val name: String,

    @Column(name = "isbn", length = 17)
    val isbn: String,

    @Column(name = "pages")
    val pages: Int,

    @Column(name = "chapters")
    val chapters: Int,

    @Column(name = "release_date")
    val releaseDate: Date,

    @Column(name = "is_active")
    val isActive: Boolean = true,

    @ManyToOne(cascade = [CascadeType.MERGE])
    val author: AuthorEntity,

    @ManyToOne(cascade = [CascadeType.MERGE])
    val publisher: PublisherEntity,

    @ManyToOne(cascade = [CascadeType.MERGE])
    val user: UserEntity
) : AuditableEntity()
