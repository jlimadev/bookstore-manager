package com.jlima.bookstoremanager.controller.auth

import com.jlima.bookstoremanager.dto.auth.AuthRequest
import com.jlima.bookstoremanager.dto.auth.AuthResponse
import com.jlima.bookstoremanager.service.authentication.AuthenticationService
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/authenticate")
@Api(tags = ["Authentication"], description = "Authenticate on System")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping
    fun authenticate(@RequestBody @Valid authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity(authenticationService.createJwtToken(authRequest), HttpStatus.OK)
    }
}
