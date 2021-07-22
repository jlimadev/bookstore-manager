package com.jlima.bookstoremanager.providers.entity.domain

import com.jlima.bookstoremanager.dto.PublisherDTO
import com.jlima.bookstoremanager.providers.entity.AuditableEntity
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
    var name: String,

    @Column(name = "code")
    var code: String,

    @Column(name = "foundation_date")
    @Temporal(TemporalType.TIMESTAMP)
    var foundationDate: Date,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
    val books: List<BookEntity> = listOf(),
) : AuditableEntity()

fun PublisherDTO.toEntity() = PublisherEntity(
    name = this.name,
    code = this.code,
    foundationDate = this.foundationDate
)

fun PublisherEntity.toDTO() = PublisherDTO(
    id = this.id.toString(),
    name = this.name,
    code = this.code,
    foundationDate = this.foundationDate
)
