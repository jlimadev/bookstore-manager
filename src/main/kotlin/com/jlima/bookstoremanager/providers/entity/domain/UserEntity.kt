package com.jlima.bookstoremanager.providers.entity.domain

import com.jlima.bookstoremanager.core.domain.Gender
import com.jlima.bookstoremanager.providers.entity.AuditableEntity
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(schema = "domain", name = "user")
data class UserEntity(
    @Column(name = "name")
    var name: String,

    @Column(name = "gender", length = 6)
    @Enumerated(EnumType.STRING)
    var gender: Gender,

    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    var birthDate: Date,

    @Column(name = "email")
    var email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "role")
    var role: String,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var books: List<BookEntity>,
) : AuditableEntity()
