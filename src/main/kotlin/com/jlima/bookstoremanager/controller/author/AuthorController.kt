package com.jlima.bookstoremanager.controller.author

import com.jlima.bookstoremanager.dto.author.AuthorDTO
import com.jlima.bookstoremanager.dto.response.CustomMessageResponse
import com.jlima.bookstoremanager.dto.response.PaginationResponse
import com.jlima.bookstoremanager.service.AuthorService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
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
    override fun findAll(
        @PageableDefault(page = 0, size = 10)
        @SortDefault.SortDefaults(
            SortDefault(sort = ["name"], direction = Sort.Direction.ASC)
        ) pageable: Pageable
    ): ResponseEntity<PaginationResponse<AuthorDTO>> {
        return ResponseEntity(authorService.findAll(pageable), HttpStatus.OK)
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

    @DeleteMapping("/{id}/soft")
    override fun deleteSoft(@PathVariable id: UUID): ResponseEntity<CustomMessageResponse> {
        val responseMessage = authorService.deleteSoft(id)
        return ResponseEntity(CustomMessageResponse(message = responseMessage), HttpStatus.OK)
    }
}
