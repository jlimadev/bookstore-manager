package com.jlima.bookstoremanager.controller.publisher

import com.jlima.bookstoremanager.dto.publisher.PublisherDTO
import com.jlima.bookstoremanager.dto.response.PaginationResponse
import com.jlima.bookstoremanager.exception.model.AvailableEntities
import com.jlima.bookstoremanager.exception.model.BusinessEmptyResponseException
import com.jlima.bookstoremanager.exception.model.BusinessEntityNotFoundException
import com.jlima.bookstoremanager.helper.toJson
import com.jlima.bookstoremanager.service.PublisherService
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasItems
import org.hamcrest.core.Is
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.Instant
import java.time.Period
import java.util.Date
import java.util.UUID

@ExtendWith(SpringExtension::class)
@WebMvcTest(PublisherController::class)
// @MockBeans(
//    MockBean(AuthenticationService::class),
//    MockBean(JwtTokenProvider::class),
//    MockBean(JwtAuthenticationEntrypoint::class)
// )
// @WithMockUser(roles = ["ADMIN", "USER"])
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
            val expectedResponse = "Entity not found! PUBLISHER $invalidId not found. Please check you request."

            whenever(publisherService.findById(invalidId)).thenThrow(
                BusinessEntityNotFoundException(
                    entity = AvailableEntities.PUBLISHER,
                    identifier = invalidId.toString()
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

    @Nested
    @DisplayName("[GET] - FindAll")
    inner class FindAll {
        @Test
        fun `It should return status 200 (OK) and a list of Publishers when getAll with Custom Pageable parameters`() {
            // Arrange
            val (defaultDTO, entityId) = makeSut()
            val customPageable = PageRequest.of(0, 15, Sort.by("any").descending())
            val listOfEntities = listOf(defaultDTO.copy(id = entityId.toString()))
            val expectedPaginationResponse = PaginationResponse(
                totalPages = 1,
                totalItems = 1,
                currentPage = 0,
                currentItems = 1,
                data = listOfEntities
            )

            // Act
            whenever(publisherService.findAll(customPageable)).thenReturn(expectedPaginationResponse)

            // Assert
            mockMvc.get(basePath) {
                contentType = MediaType.APPLICATION_JSON
                param("page", "0")
                param("size", "15")
                param("sort", "any,desc")
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { json(expectedPaginationResponse.toJson()) }
                }
        }

        @Test
        fun `It should return status 200 (OK) and call getAll using default pageable params when nothing is passed`() {
            // Arrange
            val defaultPageable = PageRequest.of(0, 10, Sort.by("name").ascending())

            // Assert
            mockMvc.get(basePath)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                }

            verify(publisherService, times(1)).findAll(defaultPageable)
        }

        @Test
        fun `It should not return 404 (Not Found) when GET returns nothing`() {
            // Arrange
            val expectedErrorMessage = "Entity not found! No PUBLISHER(s) found. Please check you request."

            // Act
            whenever(publisherService.findAll(any())).thenThrow(
                BusinessEmptyResponseException(AvailableEntities.PUBLISHER)
            )

            // Assert
            mockMvc.get(basePath)
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.statusCode", Is.`is`(404))
                    jsonPath("$.error", Is.`is`("Not Found"))
                    jsonPath("$.message", CoreMatchers.equalTo(expectedErrorMessage))
                }
        }
    }

    @Nested
    @DisplayName("[PUT] - Update")
    inner class Update {
        @Test
        fun `It should update and return status 200 (OK) when send existing id and valid body`() {
            // Arrange
            val (defaultDTO, entityId) = makeSut()
            val requestBodyToUpdate = defaultDTO.copy(
                name = "Updated name",
                code = "Updated Code",
                foundationDate = Date.from(Instant.now())
            )
            val expectedUpdatedResponse = requestBodyToUpdate.copy(id = entityId.toString())

            // Act
            whenever(publisherService.update(entityId, requestBodyToUpdate)).thenReturn(expectedUpdatedResponse)

            // Assert
            mockMvc.put("$basePath/$entityId") {
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

        @Test
        fun `It should return status 400 (Bad Request) when PUT with no JSON body`() {
            // Arrange
            val sutData = makeSut()
            val expectedErrorMessage = "Malformed JSON body. Check you JSON and try again."

            // Assert
            mockMvc.put("$basePath/${sutData.entityId}")
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.statusCode", CoreMatchers.equalTo(400))
                    jsonPath("$.error", CoreMatchers.equalTo("Bad Request"))
                    jsonPath("$.message", CoreMatchers.equalTo(expectedErrorMessage))
                }
        }

        @Test
        fun `It should return status 400 (Bad Request) when PUT with invalid data on JSON body`() {
            // Arrange
            val (defaultDTO, entityId) = makeSut()
            val requestBodyToUpdate = defaultDTO.copy(name = "")
            val expectedContainingErrors = listOf(
                "Field: NAME: must not be empty",
                "Field: NAME: size must be between 3 and 255"
            )

            // Assert
            mockMvc.put("$basePath/$entityId") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBodyToUpdate.toJson()
            }
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.statusCode", Is.`is`(400))
                    jsonPath("$.error", Is.`is`("Bad Request"))
                    jsonPath("$.message", Is.`is`("Validation error, please check the arguments."))
                    jsonPath(
                        "$.errors",
                        CoreMatchers.hasItems(expectedContainingErrors[0], expectedContainingErrors[1])
                    )
                }
        }

        @Test
        fun `It should return status 404 (Not Found) when PUT with non-existing id`() {
            // Arrange
            val (defaultDTO) = makeSut()
            val nonExistingId = UUID.randomUUID()

            // Act
            whenever(publisherService.update(nonExistingId, defaultDTO)).thenThrow(
                BusinessEntityNotFoundException(
                    entity = AvailableEntities.AUTHOR,
                    identifier = nonExistingId.toString()
                )
            )

            // Assert
            mockMvc.put("$basePath/$nonExistingId") {
                contentType = MediaType.APPLICATION_JSON
                content = defaultDTO.toJson()
            }
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.statusCode", Is.`is`(404))
                    jsonPath("$.error", Is.`is`("Not Found"))
                }
        }
    }

    @Nested
    @DisplayName("[DELETE] - Delete - Hard")
    inner class Delete {
        @Test
        fun `It should delete and return status 200 (OK) when send existing id to delete`() {
            // Arrange
            val (_, entityId) = makeSut()
            val expectedMessage = "Any deleted message"

            whenever(publisherService.delete(entityId)).thenReturn(expectedMessage)

            // Assert
            mockMvc.delete("$basePath/$entityId")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.message", CoreMatchers.equalTo(expectedMessage))
                }
            verify(publisherService, times(1)).delete(entityId)
        }

        @Test
        fun `It should return status 404 (Not Found) when DELETE with non-existing id`() {
            // Arrange
            val nonExistingId = UUID.randomUUID()

            // Act
            whenever(publisherService.delete(nonExistingId)).thenThrow(
                BusinessEntityNotFoundException(
                    entity = AvailableEntities.AUTHOR,
                    identifier = nonExistingId.toString()
                )
            )

            // Assert
            mockMvc.delete("$basePath/$nonExistingId")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.statusCode", CoreMatchers.equalTo(404))
                    jsonPath("$.error", CoreMatchers.equalTo("Not Found"))
                }
            verify(publisherService, times(1)).delete(nonExistingId)
        }
    }

    @Nested
    @DisplayName("[DELETE] - Delete - Soft")
    inner class DeleteSoft {
        @Test
        fun `It should delete and return status 200 (OK) when send existing id to delete`() {
            // Arrange
            val (_, entityId) = makeSut()
            val expectedMessage = "Any deleted message"

            whenever(publisherService.deleteSoft(entityId)).thenReturn(expectedMessage)

            // Assert
            mockMvc.delete("$basePath/$entityId/soft")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.message", CoreMatchers.equalTo(expectedMessage))
                }
            verify(publisherService, times(1)).deleteSoft(entityId)
        }

        @Test
        fun `It should return status 404 (Not Found) when DELETE with non-existing id`() {
            // Arrange
            val nonExistingId = UUID.randomUUID()

            // Act
            whenever(publisherService.deleteSoft(nonExistingId)).thenThrow(
                BusinessEntityNotFoundException(
                    entity = AvailableEntities.AUTHOR,
                    identifier = nonExistingId.toString()
                )
            )

            // Assert
            mockMvc.delete("$basePath/$nonExistingId/soft")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.statusCode", CoreMatchers.equalTo(404))
                    jsonPath("$.error", CoreMatchers.equalTo("Not Found"))
                }
            verify(publisherService, times(1)).deleteSoft(nonExistingId)
        }
    }
}
