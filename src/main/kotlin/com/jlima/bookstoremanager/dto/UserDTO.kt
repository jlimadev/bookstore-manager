package com.jlima.bookstoremanager.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.jlima.bookstoremanager.core.Gender
import com.jlima.bookstoremanager.helper.ValidUUID
import java.util.Date
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

    @field:NotNull
    @field:NotEmpty
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

    @field:NotNull
    @field:NotEmpty
    val role: String,
)