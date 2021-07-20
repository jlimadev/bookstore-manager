package com.jlima.bookstoremanager.dto.response

import org.springframework.data.domain.Page

data class PaginationResponse<T>(
    val totalPages: Int,
    val totalItems: Int,
    val currentPage: Int,
    val currentItems: Int,
    val data: List<T>
)

fun <T> Page<*>.toPaginationResponse(data: List<T>): PaginationResponse<T> {
    return PaginationResponse(
        totalPages = this.totalPages,
        totalItems = this.totalElements.toInt(),
        currentPage = this.number,
        currentItems = this.numberOfElements,
        data = data
    )
}
