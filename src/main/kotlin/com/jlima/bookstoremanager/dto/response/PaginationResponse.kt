package com.jlima.bookstoremanager.dto.response

data class PaginationResponse<T>(
    val totalPages: Int,
    val totalItems: Int,
    val currentPage: Int,
    val data: List<T>
)
