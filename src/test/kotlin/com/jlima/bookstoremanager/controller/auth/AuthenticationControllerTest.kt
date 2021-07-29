package com.jlima.bookstoremanager.controller.auth

import com.jlima.bookstoremanager.config.security.JwtAuthenticationEntrypoint
import com.jlima.bookstoremanager.dto.auth.AuthRequest
import com.jlima.bookstoremanager.dto.auth.AuthResponse
import com.jlima.bookstoremanager.exception.model.BusinessAuthenticationException
import com.jlima.bookstoremanager.helper.toJson
import com.jlima.bookstoremanager.service.authentication.AuthenticationService
import com.jlima.bookstoremanager.service.authentication.JwtTokenProvider
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.MockBeans
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@ExtendWith(SpringExtension::class)
@WebMvcTest(AuthenticationController::class)
@MockBeans(
    MockBean(JwtTokenProvider::class),
    MockBean(JwtAuthenticationEntrypoint::class)
)
class AuthenticationControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authenticationService: AuthenticationService

    private val basePath = "/authenticate"

    @Test
    fun `It should authenticate and return status 200 (ok) when post with valid data`() {
        // Arrange
        val authRequest = AuthRequest(username = "any@mail.com", password = "anyPassword")
        val authResponse = AuthResponse(accessToken = "myAccessToken")
        whenever(authenticationService.createJwtToken(authRequest)).thenReturn(authResponse)

        // Act and Assert
        mockMvc.post(basePath) {
            contentType = MediaType.APPLICATION_JSON
            content = authRequest.toJson()
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.accessToken", equalTo(authResponse.accessToken))
            }
    }

    @Test
    fun `It should return status 401 (Unauthorized) when Post non-existing credentials`() {
        // Arrange
        val invalidRequest = AuthRequest(username = "invalid@mail.com", password = "wrongPass")
        whenever(authenticationService.createJwtToken(invalidRequest))
            .thenThrow(BusinessAuthenticationException("Any"))

        // Act and Assert
        mockMvc.post(basePath) {
            contentType = MediaType.APPLICATION_JSON
            content = invalidRequest.toJson()
        }
            .andDo { print() }
            .andExpect {
                status { isUnauthorized() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.error", equalTo("Unauthorized"))
            }
    }

    @Test
    fun `It should return status 400 (BadRequest) when post with invalid data`() {
        // Arrange
        val invalidRequest = AuthRequest(username = "", password = "wrongPass")
        val expectedErrorMessage = "Field: USERNAME: must not be empty"
        whenever(authenticationService.createJwtToken(invalidRequest))
            .thenThrow(BusinessAuthenticationException("Any"))

        // Act and Assert
        mockMvc.post(basePath) {
            contentType = MediaType.APPLICATION_JSON
            content = invalidRequest.toJson()
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.error", equalTo("Bad Request"))
                jsonPath("$.errors", hasItem(expectedErrorMessage))
            }
    }
}
