package com.jlima.bookstoremanager.controller.author

import com.jlima.bookstoremanager.controller.BaseController
import com.jlima.bookstoremanager.dto.AuthorDTO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity

@Api("Authors Management")
interface AuthorControllerDocs : BaseController<AuthorDTO> {
    @ApiOperation(value = "Create a new author")
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Author created successfully!"),
            ApiResponse(code = 400, message = "Missing required fields, wrong field range value or author already registered on system")
        ]
    )
    override fun create(entity: AuthorDTO): ResponseEntity<AuthorDTO>
}