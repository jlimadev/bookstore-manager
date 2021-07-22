package com.jlima.bookstoremanager.controller.publisher

import com.jlima.bookstoremanager.controller.BaseController
import com.jlima.bookstoremanager.dto.PublisherDTO
import com.jlima.bookstoremanager.dto.response.CustomMessageResponse
import com.jlima.bookstoremanager.dto.response.PaginationResponse
import com.jlima.bookstoremanager.service.PublisherService
import io.swagger.annotations.Api
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

@RestController
@RequestMapping("/publishers")
@Api(tags = ["Publishers"], description = "Endpoint to manage all publishers within the system")
class PublisherController(
    private val publisherService: PublisherService
) : BaseController<PublisherDTO> {
    @PostMapping
    override fun create(@RequestBody entity: PublisherDTO): ResponseEntity<PublisherDTO> {
        return ResponseEntity(publisherService.create(entity), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    override fun findById(@PathVariable id: UUID): ResponseEntity<PublisherDTO> {
        return ResponseEntity(publisherService.findById(id), HttpStatus.OK)
    }

    @GetMapping
    override fun findAll(
        @PageableDefault(page = 0, size = 10)
        @SortDefault.SortDefaults(
            SortDefault(sort = ["name"], direction = Sort.Direction.ASC)
        )
        pageable: Pageable
    ): ResponseEntity<PaginationResponse<PublisherDTO>> {
        return ResponseEntity(publisherService.findAll(pageable), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    override fun update(@PathVariable id: UUID, @RequestBody body: PublisherDTO): ResponseEntity<PublisherDTO> {
        return ResponseEntity(publisherService.update(id, body), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    override fun delete(@PathVariable id: UUID): ResponseEntity<CustomMessageResponse> {
        return ResponseEntity(CustomMessageResponse(publisherService.delete(id)), HttpStatus.OK)
    }

    @DeleteMapping("/{id}/soft")
    override fun deleteSoft(@PathVariable id: UUID): ResponseEntity<CustomMessageResponse> {
        return ResponseEntity(CustomMessageResponse(publisherService.deleteSoft(id)), HttpStatus.OK)
    }
}
