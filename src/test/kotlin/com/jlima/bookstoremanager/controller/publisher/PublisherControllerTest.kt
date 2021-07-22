package com.jlima.bookstoremanager.controller.publisher

import com.jlima.bookstoremanager.dto.PublisherDTO
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.helper.toJson
import com.jlima.bookstoremanager.service.PublisherService
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasItems
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.Instant
import java.time.Period
import java.util.Date
import java.util.UUID

@ExtendWith(SpringExtension::class)
@WebMvcTest(PublisherController::class)
internal class PublisherControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var publisherService: PublisherService

    private val basePath = "/publishers"

    private data class SUT(
        val defaultDTO: PublisherDTO,
        val entityId: UUID,
    )

    private fun makeSut(): SUT {
        val entityId = UUID.randomUUID()
        val defaultDTO = PublisherDTO(
            name = "Any Publisher Name",
            code = "Any publisher code",
            foundationDate = Date.from(Instant.now())
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
        fun `It return status 201 (Created) when POST with valid data`() {
            // Arrange
            val (defaultDTO, entityId) = makeSut()
            val expectedResponse = defaultDTO.copy(id = entityId.toString())

            whenever(publisherService.create(defaultDTO)).thenReturn(expectedResponse)

            // Assert
            mockMvc.post(basePath) {
                contentType = MediaType.APPLICATION_JSON
                content = defaultDTO.toJson()
            }
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { json(expectedResponse.toJson()) }
                }
        }

        @Test
        fun `It should return status 400 (BadRequest) when POST with invalid data`() {
            // Arrange
            val (defaultDTO) = makeSut()
            val futureDate = Date.from(Instant.now().plus(Period.ofDays(10)))
            val invalidDTO = defaultDTO.copy(name = "", code = "", foundationDate = futureDate)
            val nameExpectedError = "Field: NAME: must not be empty"
            val codeExpectedError = "Field: CODE: must not be empty"
            val dateExpectedError = "Field: FOUNDATIONDATE: must be a date in the past or in the present"

            // Act
            mockMvc.post(basePath) {
                contentType = MediaType.APPLICATION_JSON
                content = invalidDTO.toJson()
            }
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.errors", hasItems(nameExpectedError, codeExpectedError, dateExpectedError))
                }
        }
    }

    @Nested
    @DisplayName("[GET] - findById")
    inner class FindById {
        @Test
        fun `It should return Status 200 (OK) when call with valid id`() {
            // Arrange
            val (defaultDTO, entityId) = makeSut()
            val expectedResponse = defaultDTO.copy(id = entityId.toString())
            whenever(publisherService.findById(entityId)).thenReturn(expectedResponse)

            // Assert
            mockMvc.get("$basePath/$entityId")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { json(expectedResponse.toJson()) }
                }
        }

        @Test
        fun `It should return Status 400 (Bad Request) when call with invalid uuid`() {
            // Arrange
            val expectedResponse = "Field ID: Invalid UUID string: anythingButId"

            // Assert
            mockMvc.get("$basePath/anythingButId")
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.errors", hasItem(expectedResponse))
                }
        }

        @Test
        fun `It should return Status 404 (Not Found) when call with non-existing entity`() {
            // Arrange
            val invalidId = UUID.randomUUID()
            val expectedResponse = "Entity not found! PUBLISHER with id $invalidId not found. Please check you request."

            whenever(publisherService.findById(invalidId)).thenThrow(
                BusinessEntityNotFoundException(
                    entity = AvailableEntities.PUBLISHER,
                    id = invalidId
                )
            )

            // Assert
            mockMvc.get("$basePath/$invalidId")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    jsonPath("$.message", equalTo(expectedResponse))
                }
        }
    }

    @Test
    fun findAll() {
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun deleteSoft() {
    }
}
