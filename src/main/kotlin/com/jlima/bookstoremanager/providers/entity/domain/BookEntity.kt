package com.jlima.bookstoremanager.providers.entity.domain

import com.jlima.bookstoremanager.providers.entity.AuditableEntity
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.Date
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

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

    @ManyToOne(cascade = [CascadeType.MERGE])
    val author: AuthorEntity,

    @ManyToOne(cascade = [CascadeType.MERGE])
    val publisher: PublisherEntity,

    @ManyToOne(cascade = [CascadeType.MERGE])
    val user: UserEntity,

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    override val createdAt: Date,

    @Column(name = "updated_at", nullable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    override val updatedAt: Date? = null,

    @Column(name = "deleted_at", nullable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    override val deletedAt: Date? = null
) : AuditableEntity()
