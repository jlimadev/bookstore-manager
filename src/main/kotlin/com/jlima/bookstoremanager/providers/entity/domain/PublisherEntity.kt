package com.jlima.bookstoremanager.providers.entity.domain

import com.jlima.bookstoremanager.providers.entity.AuditableEntity
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
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
    val books: List<BookEntity>,

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
