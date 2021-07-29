package com.jlima.bookstoremanager.exception

import com.jlima.bookstoremanager.exception.model.BusinessAuthenticationException
import com.jlima.bookstoremanager.exception.model.ExceptionResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Instant
import java.util.Date
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(
        value = [
            EntityNotFoundException::class,
            EntityExistsException::class,
            MethodArgumentTypeMismatchException::class,
            AuthenticationException::class
        ]
    )
    fun customExceptionHandler(exception: Exception, request: WebRequest): ResponseEntity<Any> {
        val statusCode: HttpStatus
        val message: String
        val errorList = mutableListOf<String>()

        when (exception) {
            is EntityNotFoundException -> {
                statusCode = HttpStatus.NOT_FOUND
                message = "Entity not found!"
            }
            is EntityExistsException -> {
                statusCode = HttpStatus.CONFLICT
                message = "This entity already exists!"
            }
            is MethodArgumentTypeMismatchException -> {
                statusCode = HttpStatus.BAD_REQUEST
                message = "Invalid argument."
                errorList.add("Field ${exception.name.uppercase()}: ${exception.mostSpecificCause.message}")
            }
            is BusinessAuthenticationException -> {
                statusCode = HttpStatus.UNAUTHORIZED
                message = "Check your credentials."
            }
            else -> {
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR
                message = "Unexpected error!"
            }
        }

        val exceptionMessage = "$message ${exception.message} Please check you request."
        val errorBody = buildExceptionEntity(
            httpStatus = statusCode,
            message = exceptionMessage,
            errors = errorList
        )
        return handleExceptionInternal(exception, errorBody, HttpHeaders(), statusCode, request)
    }

    override fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val statusCode = HttpStatus.BAD_REQUEST
        val message = "Validation error, please check the arguments."
        val fieldErrors = exception.fieldErrors.map { "Field: ${it.field.uppercase()}: ${it.defaultMessage}" }
        val globalErrors = exception.globalErrors.map { "Object: ${it.objectName.uppercase()}: ${it.defaultMessage}" }
        val errorBody = buildExceptionEntity(
            httpStatus = statusCode,
            message = message,
            errors = fieldErrors.plus(globalErrors)
        )
        return handleExceptionInternal(exception, errorBody, HttpHeaders(), statusCode, request)
    }

    override fun handleHttpMessageNotReadable(
        exception: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val statusCode = HttpStatus.BAD_REQUEST
        val message = "Malformed JSON body. Check you JSON and try again."
        val errorBody = buildExceptionEntity(
            httpStatus = statusCode,
            message = message,
            errors = listOf(exception.message?.split(";")?.get(0) ?: "")
        )
        return handleExceptionInternal(exception, errorBody, HttpHeaders(), statusCode, request)
    }

    private fun buildExceptionEntity(
        httpStatus: HttpStatus,
        message: String,
        errors: List<String> = listOf()
    ): ExceptionResponse {
        return ExceptionResponse(
            statusCode = httpStatus.value(),
            error = httpStatus.reasonPhrase,
            message = message,
            errors = errors,
            timestamp = Date.from(Instant.now())
        )
    }
}
