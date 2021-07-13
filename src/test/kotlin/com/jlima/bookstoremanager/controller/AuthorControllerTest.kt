package com.jlima.bookstoremanager.controller

import com.jlima.bookstoremanager.controller.author.AuthorController
import com.jlima.bookstoremanager.dto.AuthorDTO
import com.jlima.bookstoremanager.helper.toJson
import com.jlima.bookstoremanager.service.AuthorService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.Instant
import java.util.Date
import java.util.UUID

@ExtendWith(SpringExtension::class)
@WebMvcTest(AuthorController::class)
class AuthorControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authorService: AuthorService

    private data class SUT(
        val defaultDTO: AuthorDTO,
        val entityId: UUID,
    )

    private fun makeSut(): SUT {
        val entityId = UUID.randomUUID()
        val defaultDTO = AuthorDTO(
            name = "Jonathan Lima",
            birthDate = Date.from(Instant.now())
        )

        return SUT(
            defaultDTO = defaultDTO,
            entityId = entityId
        )
    }

    @Test
    fun `It should created an Author and Return 201 (Created) when POST`() {
        // Given [Arrange]
        val (defaultDTO, entityId) = makeSut()
        val expectedResponse = defaultDTO.copy(id = entityId.toString())

        // When [Act]
        whenever(authorService.create(any())).thenReturn(expectedResponse)

        // Then [Assert]
        mockMvc
            .post("/api/v1/authors") {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
                content = defaultDTO.toJson()
            }.andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(expectedResponse.toJson()) }
            }
    }
}
