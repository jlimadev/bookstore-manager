package com.jlima.bookstoremanager.providers.entity.domain

import com.jlima.bookstoremanager.dto.user.UserDTO
import com.jlima.bookstoremanager.enums.Gender
import com.jlima.bookstoremanager.enums.Role
import com.jlima.bookstoremanager.providers.entity.AuditableEntity
import java.util.Date
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(schema = "domain", name = "user")
data class UserEntity(
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "name")
    var name: String,

    @Column(name = "gender", length = 6)
    @Enumerated(EnumType.STRING)
    var gender: Gender,

    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    var birthDate: Date,

    @Column(name = "email", unique = true)
    var email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: Role,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var books: List<BookEntity> = listOf(),
) : AuditableEntity()

fun UserDTO.toEntity() = UserEntity(
    name = this.name,
    gender = this.gender,
    birthDate = this.birthDate,
    email = this.email,
    password = this.password,
    role = this.role
)

fun UserEntity.toDTO() = UserDTO(
    id = this.id.toString(),
    name = this.name,
    gender = this.gender,
    birthDate = this.birthDate,
    email = this.email,
    password = this.password,
    role = this.role
)
