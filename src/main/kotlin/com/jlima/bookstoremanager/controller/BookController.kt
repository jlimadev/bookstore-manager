package com.jlima.bookstoremanager.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/books")
class BookController {

    @GetMapping
    fun hello(): ResponseEntity<String> {
        return ResponseEntity("Hello world!!", HttpStatus.OK)
    }
}
