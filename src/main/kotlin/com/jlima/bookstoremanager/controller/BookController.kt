package com.jlima.bookstoremanager.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/books")
class BookController {
    @ApiOperation("This method returns a hello world")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Success method return")])
    @GetMapping
    fun hello(): ResponseEntity<String> {
        return ResponseEntity("Hello world, new version!", HttpStatus.OK)
    }
}
