package com.jlima.bookstoremanager.dto.auth

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class AuthRequest(
    @field:NotNull
    @field:NotEmpty
    val username: String,

    @field:NotNull
    @field:NotEmpty
    val password: String
)
