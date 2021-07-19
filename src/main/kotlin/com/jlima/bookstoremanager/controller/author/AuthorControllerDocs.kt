package com.jlima.bookstoremanager.controller.author

import com.jlima.bookstoremanager.controller.BaseController
import com.jlima.bookstoremanager.dto.AuthorDTO
import com.jlima.bookstoremanager.dto.response.CustomMessageResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import java.util.UUID

@Api(tags = ["Authors"], description = "Endpoint to manage all authors within the system")
interface AuthorControllerDocs : BaseController<AuthorDTO> {
    @ApiOperation(value = "Create a new author")
    @ApiResponses(
        value = [
            ApiResponse(
                code = 201,
                message = "Author created successfully"
            ),
            ApiResponse(
                code = 400,
                message = "Missing required fields, wrong field range value or author already registered on system"
            )
        ]
    )
    override fun create(entity: AuthorDTO): ResponseEntity<AuthorDTO>

    @ApiOperation(value = "Find an author by id")
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "Author found"
            ),
            ApiResponse(
                code = 404,
                message = "Author not found"
            )
        ]
    )
    override fun findById(id: UUID): ResponseEntity<AuthorDTO>

    @ApiOperation(value = "Find all authors")
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "Authors found!"
            ),
            ApiResponse(
                code = 404,
                message = "No authors found"
            )
        ]
    )
    override fun findAll(): ResponseEntity<List<AuthorDTO>>

    @ApiOperation(value = "Update an Author by id")
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "Author successfully updated"
            ),
            ApiResponse(
                code = 400,
                message = "Missing required fields, wrong field range value or author already registered on system"
            ),
            ApiResponse(
                code = 404,
                message = "Author not found"
            )
        ]
    )
    override fun update(id: UUID, body: AuthorDTO): ResponseEntity<AuthorDTO>

    @ApiOperation(value = "Delete an Author by id (isActive = false)")
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "Author successfully deleted"
            ),
            ApiResponse(
                code = 404,
                message = "Author not found"
            )
        ]
    )
    override fun delete(id: UUID): ResponseEntity<CustomMessageResponse>

    @ApiOperation(value = "Delete an Author by id (hard delete)")
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "Author successfully deleted"
            ),
            ApiResponse(
                code = 404,
                message = "Author not found"
            )
        ]
    )
    override fun hardDelete(id: UUID): ResponseEntity<CustomMessageResponse>
}
