package com.jlima.bookstoremanager.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info


@OpenAPIDefinition(
    info = Info(
        title = "Bookstore Manager API",
        description = "Bookstore Manager Project",
        version = "1.0.0",
        contact = Contact(name = "Jonathan Lima", url = "https://github.com/jlimadev", email = "jlima.dev@gmail.com")
    ),
)
class SpringdocConfig