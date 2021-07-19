package com.jlima.bookstoremanager.controller.author

import com.jlima.bookstoremanager.dto.AuthorDTO
import com.jlima.bookstoremanager.dto.response.CustomMessageResponse
import com.jlima.bookstoremanager.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/authors")
class AuthorController(
    private val authorService: AuthorService
) : AuthorControllerDocs {

    @PostMapping
    override fun create(@RequestBody @Valid entity: AuthorDTO): ResponseEntity<AuthorDTO> {
        val createdAuthor = authorService.create(entity)
        return ResponseEntity(createdAuthor, HttpStatus.CREATED)
    }

    @GetMapping(path = ["/{id}"])
    override fun findById(@PathVariable id: UUID): ResponseEntity<AuthorDTO> {
        return ResponseEntity(authorService.findById(id), HttpStatus.OK)
    }

    @GetMapping
    override fun findAll(): ResponseEntity<List<AuthorDTO>> {
        return ResponseEntity(authorService.findAll(), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    override fun update(@PathVariable id: UUID, @RequestBody @Valid body: AuthorDTO): ResponseEntity<AuthorDTO> {
        return ResponseEntity(authorService.update(id, body), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    override fun delete(@PathVariable id: UUID): ResponseEntity<CustomMessageResponse> {
        val responseMessage = authorService.delete(id)
        return ResponseEntity(CustomMessageResponse(message = responseMessage), HttpStatus.OK)
    }

    @DeleteMapping("/{id}/hard")
    override fun hardDelete(@PathVariable id: UUID): ResponseEntity<CustomMessageResponse> {
        val responseMessage = authorService.hardDelete(id)
        return ResponseEntity(CustomMessageResponse(message = responseMessage), HttpStatus.OK)
    }
}
