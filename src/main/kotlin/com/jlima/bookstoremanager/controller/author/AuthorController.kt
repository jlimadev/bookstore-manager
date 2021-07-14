package com.jlima.bookstoremanager.controller.author

import com.jlima.bookstoremanager.dto.AuthorDTO
import com.jlima.bookstoremanager.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/authors")
class AuthorController(
    private val authorService: AuthorService
) : AuthorControllerDocs {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    override fun create(@RequestBody @Valid entity: AuthorDTO): ResponseEntity<AuthorDTO> {
        val createdAuthor = authorService.create(entity)
        return ResponseEntity(createdAuthor, HttpStatus.CREATED)
    }
}
