# Global Exception Handler

The `@ControllerAdvice` [annotation](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc) deals with
errors on the request, by injecting the exception handler, or other actions when an exception happens. The interceptor
of exceptions thrown by methods annotated with @RequestMapping or one of the shortcuts (Understanding Better
@ControllerAdvice check the references.)

This document explains how the GlobalExceptionHandler.kt file works.

The @ControllerAdvice will intercept errors from controllers, generally they contain:
- `@RequestMapping`
    - `@GetMapping`
    - `@PostMapping`
    - `@PutMapping`
    - `@PatchMapping`
    - `@DeleteMapping`

Supported annotations to the methods are:

- `@ExceptionHandler`
- `@ModelAttribute`
- `@InitBinder`

Our class need to have this @ControllerAdvice annotation and extends ResponseEntityExceptionHandler*. Inside we can have
methods (or a generic one) to handle the exceptions. This method have to annotated with `@ExceptionHandler`, and we pass
the possible exceptions it will handle. (must be inherited from Exception class).

**ResponseEntityExceptionHandler***: Is a class with exception handler methods we can override, to create our own custom
errors.

The example bellow we have a generic method to handle EntityNotFoundException, EntityExistsException. (We can add as
many as we want, since it is an Exception class). Based on the type we send a specific result.

```kotlin
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
}
```

Overriding methods from ResponseEntityExceptionHandler:

```kotlin
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
```

For reference, this is the buildExceptionEntity

```kotlin
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
```

## References:

- [Medium Article](https://medium.com/@jovannypcg/understanding-springs-controlleradvice-cd96a364033f)
- [Baeldung Article/Solution 3](https://www.baeldung.com/exception-handling-for-rest-with-spring)
- [ZetCode Article](https://zetcode.com/springboot/controlleradvice/)

