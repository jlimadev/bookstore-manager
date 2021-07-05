package com.jlima.bookstoremanager.providers.entity

import com.jlima.bookstoremanager.core.domain.Gender
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
    val name: String,

    @Column(name = "gender", length = 6)
    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    val birthDate: Date,

    @Column(name = "email")
    val email: String,

    @Column(name = "password")
    val password: String,

    @Column(name = "role")
    val role: String,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val books: List<BookEntity>
)
