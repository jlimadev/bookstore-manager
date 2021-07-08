package com.jlima.bookstoremanager.exception

import java.util.Date

data class ApiError(
    private val statusCode: Int,
    private val error: String,
    private val timestamp: Date,
    private val message: String,
    private val errors: List<String>
)
