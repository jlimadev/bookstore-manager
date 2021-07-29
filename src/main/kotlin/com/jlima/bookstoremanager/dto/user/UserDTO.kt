package com.jlima.bookstoremanager.dto.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.jlima.bookstoremanager.enums.Gender
import com.jlima.bookstoremanager.enums.Role
import com.jlima.bookstoremanager.helper.ValidUUID
import java.util.Date
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past
import javax.validation.constraints.Size

data class UserDTO(
    @field:ValidUUID
    val id: String? = null,

    @field:NotNull
    @field:NotEmpty
    @field:Size(min = 3, max = 255)
    val name: String,

    @Enumerated(EnumType.STRING)
    @field:NotNull
    val gender: Gender,

    @field:NotNull
    @field:Past
    val birthDate: Date,

    @field:NotNull
    @field:NotEmpty
    @field:Email
    val email: String,

    @field:NotNull
    @field:NotEmpty
    @field:Size(min = 8, max = 15)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String,

    @Enumerated(EnumType.STRING)
    @field:NotNull
    val role: Role,
)
