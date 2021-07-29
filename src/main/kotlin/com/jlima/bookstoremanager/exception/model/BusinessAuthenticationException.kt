package com.jlima.bookstoremanager.exception.model

import org.springframework.security.core.AuthenticationException

data class BusinessAuthenticationException(val msg: String) : AuthenticationException(msg)
