package com.jlima.bookstoremanager.controller.user

import com.jlima.bookstoremanager.controller.BaseController
import com.jlima.bookstoremanager.dto.response.CustomMessageResponse
import com.jlima.bookstoremanager.dto.response.PaginationResponse
import com.jlima.bookstoremanager.dto.user.UserDTO
import com.jlima.bookstoremanager.service.UserService
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
import javax.validation.Valid

@RestController
@RequestMapping("/users")
@Api(tags = ["Users"], description = "Endpoint to manage all users")
class UserController(
    private val userService: UserService
) : BaseController<UserDTO> {
    @PostMapping
    override fun create(@RequestBody @Valid entity: UserDTO): ResponseEntity<UserDTO> {
        return ResponseEntity(userService.create(entity), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    override fun findById(@PathVariable id: UUID): ResponseEntity<UserDTO> {
        return ResponseEntity(userService.findById(id), HttpStatus.OK)
    }

    @GetMapping
    override fun findAll(
        @PageableDefault(page = 0, size = 10)
        @SortDefault.SortDefaults(
            SortDefault(sort = ["name"], direction = Sort.Direction.ASC)
        )
        pageable: Pageable
    ): ResponseEntity<PaginationResponse<UserDTO>> {
        return ResponseEntity(userService.findAll(pageable), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    override fun update(@PathVariable id: UUID, @RequestBody @Valid body: UserDTO): ResponseEntity<UserDTO> {
        return ResponseEntity(userService.update(id, body), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    override fun delete(@PathVariable id: UUID): ResponseEntity<CustomMessageResponse> {
        return ResponseEntity(CustomMessageResponse(userService.delete(id)), HttpStatus.OK)
    }

    @DeleteMapping("/{id}/soft")
    override fun deleteSoft(@PathVariable id: UUID): ResponseEntity<CustomMessageResponse> {
        return ResponseEntity(CustomMessageResponse(userService.deleteSoft(id)), HttpStatus.OK)
    }
}
