package com.jlima.bookstoremanager.dto.publisher

import com.jlima.bookstoremanager.helper.ValidUUID
import java.util.Date
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Size

data class PublisherDTO(
    @field:ValidUUID
    val id: String? = null,

    @field:NotNull
    @field:NotEmpty
    @field:Size(min = 3, max = 255)
    val name: String,

    @field:NotNull
    @field:NotEmpty
    @field:Size(min = 3, max = 50)
    val code: String,

    @field:NotNull
    @field:PastOrPresent
    val foundationDate: Date,
)
