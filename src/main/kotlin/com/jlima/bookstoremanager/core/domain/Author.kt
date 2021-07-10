package com.jlima.bookstoremanager.core.domain

import java.util.Date

data class Author(
    val id: String,
    val name: String,
    val birthDate: Date
)