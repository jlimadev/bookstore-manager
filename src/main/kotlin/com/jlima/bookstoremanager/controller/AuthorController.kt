package com.jlima.bookstoremanager.controller

import com.jlima.bookstoremanager.service.AuthorService
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/authors")
@Api("Authors Management")
class AuthorController(
    private val authorService: AuthorService
)
