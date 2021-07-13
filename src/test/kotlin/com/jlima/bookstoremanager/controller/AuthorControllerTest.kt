package com.jlima.bookstoremanager.controller

import com.jlima.bookstoremanager.core.domain.Author
import com.jlima.bookstoremanager.service.AuthorService
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import java.time.Instant
import java.util.Date
import java.util.UUID

@ExtendWith(MockitoExtension::class)
@WebMvcTest(AuthorController::class)
internal class AuthorControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private data class SUT(
        private val sut: AuthorController
    )

    private fun makeSut(): SUT {
        val authorService: AuthorService = mock()
        val authorDTO = Author(
            id = UUID.randomUUID().toString(),
            name = "Jonathan",
            birthDate = Date.from(Instant.now())
        )

        return SUT(
            sut = AuthorController(authorService)
        )
    }
}
