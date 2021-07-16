package com.jlima.bookstoremanager.helper

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ValidUUIDTest {
    val validator = ValidUUID.UuidValidator()

    @Nested
    @DisplayName("ValidateUUID Annotation Test")
    inner class ValidateUUID {
        @Test
        fun `It should return true when UUID is valid`() {
            // Arrange
            val validId = UUID.randomUUID().toString()

            // Act
            val isValid = validator.isValid(value = validId, null)

            // Assert
            assertTrue(isValid)
        }

        @Test
        fun `It should return false when id is not a UUID`() {
            // Arrange
            val invalidId = "banana"

            // Act
            val isValid = validator.isValid(invalidId, null)

            // Assert
            assertFalse(isValid)
        }
    }
}
