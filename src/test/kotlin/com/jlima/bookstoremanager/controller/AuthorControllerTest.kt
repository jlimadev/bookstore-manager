package com.jlima.bookstoremanager.controller

import com.jlima.bookstoremanager.controller.author.AuthorController
import com.jlima.bookstoremanager.dto.AuthorDTO
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEmptyResponseException
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.helper.toJson
import com.jlima.bookstoremanager.service.AuthorService
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.core.Is
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
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

    @Nested
    @DisplayName("[POST] - Create")
    inner class Create {
        @Test
        fun `It should created an Author and must return status 201 (Created) when POST with valid data`() {
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
                }
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { json(expectedResponse.toJson()) }
                }
            verify(authorService, times(1)).create(defaultDTO)
        }

        @Test
        fun `It should not create an author and must return status 400 (BadRequest) when POST with invalid data`() {
            // Arrange
            val (defaultDTO) = makeSut()

            // Act
            val invalidEntity = defaultDTO.copy(name = "")
            val expectedContainingErrors = listOf(
                "Field: NAME: must not be empty",
                "Field: NAME: size must be between 3 and 255"
            )

            // Assert
            mockMvc.post("/authors") {
                contentType = MediaType.APPLICATION_JSON
                content = invalidEntity.toJson()
            }
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.statusCode", Is.`is`(400))
                    jsonPath("$.error", Is.`is`("Bad Request"))
                    jsonPath("$.message", Is.`is`("Validation error, please check the arguments."))
                    jsonPath("$.errors", hasItems(expectedContainingErrors[0], expectedContainingErrors[1]))
                }
            verify(authorService, never()).create(any())
        }
    }

    @Nested
    @DisplayName("[GET] - FindById")
    inner class FindById {
        @Test
        fun `It should get an Author by id and return status 200 (OK) when GET with valid id`() {
            // Arrange
            val (defaultDTO, entityId) = makeSut()
            val expectedResponse = defaultDTO.copy(id = entityId.toString())

            // Act
            whenever(authorService.findById(entityId)).thenReturn(expectedResponse)

            // Assert
            mockMvc.get("/authors/$entityId")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { expectedResponse.toJson() }
                }
            verify(authorService, times(1)).findById(entityId)
        }

        @Test
        fun `It should return status 404 (Not Found) when GET with non-existing id`() {
            // Arrange
            val nonExistingId = UUID.randomUUID()

            // Act
            whenever(authorService.findById(nonExistingId)).thenThrow(
                BusinessEntityNotFoundException(
                    entity = AvailableEntities.AUTHOR,
                    id = nonExistingId
                )
            )

            // Assert
            mockMvc.get("/authors/$nonExistingId")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.statusCode", Is.`is`(404))
                    jsonPath("$.error", Is.`is`("Not Found"))
                }
            verify(authorService, times(1)).findById(nonExistingId)
        }

        @Test
        fun `It should return 400 Bad Request when try to findById with non-uuid`() {
            // Arrange
            val expectedContainingError = "Field ID: Invalid UUID string: non-uuid"
            // Assert
            mockMvc.get("/authors/non-uuid")
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.statusCode", equalTo(400))
                    jsonPath("$.error", equalTo("Bad Request"))
                    jsonPath("$.errors", hasItems(expectedContainingError))
                }
            verify(authorService, never()).findById(any())
        }
    }

    @Nested
    @DisplayName("[GET] - FindAll")
    inner class FindAll {
        @Test
        fun `It should return a list of Authors and status 200 (OK) when GET All`() {
            // Arrange
            val (defaultDTO, entityId) = makeSut()
            val expectedResponse = listOf(defaultDTO.copy(id = entityId.toString()))

            // Act
            whenever(authorService.findAll()).thenReturn(expectedResponse)

            // Assert
            mockMvc.get("/authors")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { json(expectedResponse.toJson()) }
                }
            verify(authorService, times(1)).findAll()
        }

        @Test
        fun `It should not return 404 (Not Found) when GET returns nothing`() {
            // Arrange
            val expectedErrorMessage = "Entity not found! No AUTHOR(s) found. Please check you request."

            // Act
            whenever(authorService.findAll()).thenThrow(
                BusinessEmptyResponseException(AvailableEntities.AUTHOR)
            )

            // Assert
            mockMvc.get("/authors")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.statusCode", Is.`is`(404))
                    jsonPath("$.error", Is.`is`("Not Found"))
                    jsonPath("$.message", equalTo(expectedErrorMessage))
                }
            verify(authorService, times(1)).findAll()
        }
    }

    @Nested
    @DisplayName("[PUT] - Update")
    inner class Update {
        @Test
        fun `It should update and return status 201 (OK) when send existing id and valid body`() {
            // Arrange
            val (defaultDTO, entityId) = makeSut()
            val requestBodyToUpdate = defaultDTO.copy(
                name = "Updated name",
                birthDate = Date.from(Instant.now())
            )
            val expectedUpdatedResponse = requestBodyToUpdate.copy(id = entityId.toString())

            // Act
            whenever(authorService.update(entityId, requestBodyToUpdate)).thenReturn(expectedUpdatedResponse)

            // Assert
            mockMvc.put("/authors/$entityId") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBodyToUpdate.toJson()
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { expectedUpdatedResponse.toJson() }
                }
        }
    }
}
