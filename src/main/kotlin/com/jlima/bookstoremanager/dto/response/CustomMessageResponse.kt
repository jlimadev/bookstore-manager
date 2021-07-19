package com.jlima.bookstoremanager.dto.response

import java.time.Instant
import java.util.Date

data class CustomMessageResponse(
    val message: String,
    val timestamp: Date? = Date.from(Instant.now())
)
