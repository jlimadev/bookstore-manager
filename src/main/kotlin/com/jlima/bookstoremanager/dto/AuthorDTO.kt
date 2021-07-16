package com.jlima.bookstoremanager.dto

import com.jlima.bookstoremanager.helper.ValidUUID
import java.util.Date
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class AuthorDTO(
    @field:ValidUUID
    val id: String? = null,

    @field:NotNull
    @field:NotEmpty
    @field:Size(min = 3, max = 255)
    val name: String,

    @field:NotNull
    val birthDate: Date
)
