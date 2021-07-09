package com.jlima.bookstoremanager.exception

import com.jlima.bookstoremanager.exception.model.ExceptionResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
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
            EntityExistsException::class
        ]
    )
    fun customExceptionHandler(exception: Exception, request: WebRequest): ResponseEntity<Any> {
        val statusCode: HttpStatus
        val message: String

        when (exception) {
            is EntityNotFoundException -> {
                statusCode = HttpStatus.NOT_FOUND
                message = "Entity not found! Please check you request."
            }
            is EntityExistsException -> {
                statusCode = HttpStatus.BAD_REQUEST
                message = "This entity already exists! Please check you request."
            }
            else -> {
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR
                message = "Unexpected error!"
            }
        }

        val exceptionMessage = "$message \n${exception.message}"
        val errorBody = buildExceptionEntity(
            httpStatus = statusCode,
            message = exceptionMessage
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
            message = message
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
