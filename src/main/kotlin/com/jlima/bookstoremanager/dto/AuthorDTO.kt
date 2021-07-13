package com.jlima.bookstoremanager.dto

import java.util.Date
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class AuthorDTO(
    val id: String? = null,

    @NotNull
    @NotEmpty
    @Size(max = 255)
    val name: String,

    @NotNull
    val birthDate: Date
)
