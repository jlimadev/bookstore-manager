package com.jlima.bookstoremanager.exception.model

import java.util.Date

data class ExceptionResponse(
    val statusCode: Int,
    val error: String,
    val message: String,
    val errors: List<String>,
    val timestamp: Date
)
