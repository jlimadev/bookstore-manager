package com.jlima.bookstoremanager.helper

import java.lang.IllegalArgumentException
import java.util.UUID
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

const val UUID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"

/**
 * Validation constraint for {@link UUID}s.
 * References:
 * java: https://stackoverflow.com/questions/37320870/is-there-a-uuid-validator-annotation
 * kotlin: https://dsebastien.net/blog/2017-12-10-kotlin-uuid-bean-validation
 */
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [])
@Retention(AnnotationRetention.RUNTIME)
@Pattern(regexp = UUID_REGEX)
annotation class ValidUUID(
    val message: String = "{invalid.uuid}",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    class UuidValidator : ConstraintValidator<ValidUUID, String> {
        override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
            return try {
                UUID.fromString(value)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }
    }
}
