package com.jlima.bookstoremanager.providers.entity.domain

import com.jlima.bookstoremanager.dto.domain.AuthorDTO
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
@Table(schema = "domain", name = "author")
data class AuthorEntity(
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "name", length = 255)
    var name: String,

    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    var birthDate: Date,

    @Column(name = "is_active")
    var isActive: Boolean? = true,

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    val books: List<BookEntity> = listOf(),
) : AuditableEntity()

fun AuthorDTO.toEntity() = AuthorEntity(
    name = this.name,
    birthDate = this.birthDate,
    books = listOf()
)

fun AuthorEntity.toDTO() = AuthorDTO(
    id = this.id.toString(),
    name = this.name,
    birthDate = this.birthDate
)
