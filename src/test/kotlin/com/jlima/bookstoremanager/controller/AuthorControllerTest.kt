package com.jlima.bookstoremanager.controller

import com.jlima.bookstoremanager.controller.author.AuthorController
import com.jlima.bookstoremanager.dto.AuthorDTO
import com.jlima.bookstoremanager.helper.toJson
import com.jlima.bookstoremanager.service.AuthorService
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.core.Is
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
        // Arrange
        val (defaultDTO, entityId) = makeSut()
        val expectedResponse = defaultDTO.copy(id = entityId.toString())

        // Act
        whenever(authorService.create(any())).thenReturn(expectedResponse)

        // Assert
        mockMvc
            .post("/authors") {
                contentType = MediaType.APPLICATION_JSON
                content = defaultDTO.toJson()
            }.andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(expectedResponse.toJson()) }
            }
    }

    @Test
    fun `It should not create an author and return a BadRequest when POST with invalid data`() {
        // Arrange
        val (defaultDTO) = makeSut()

        // Act
        val invalidEntity = defaultDTO.copy(name = "")
        val expectedContainingError = "Field: NAME: must not be empty"

        // Assert
        mockMvc.post("/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = invalidEntity.toJson()
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.statusCode", Is.`is`(400))
            jsonPath("$.error", Is.`is`("Bad Request"))
            jsonPath("$.message", Is.`is`("Validation error, please check the arguments."))
            jsonPath("$.errors", hasItem(expectedContainingError))
        }
    }
}
