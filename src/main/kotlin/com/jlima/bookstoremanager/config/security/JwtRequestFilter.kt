package com.jlima.bookstoremanager.config.security

import com.jlima.bookstoremanager.service.authentication.AuthenticationService
import com.jlima.bookstoremanager.service.authentication.JwtTokenProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter(
    private val authenticationService: AuthenticationService,
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    companion object {
        const val AUTHORIZATION_HEADER_KEY = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER_KEY)
        if (validateAuthorizationHeader(authorizationHeader)) {
            val jwtToken = authorizationHeader.split(" ")[1]
            val username = jwtTokenProvider.getClaimsFromToken(jwtToken).subject

            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                addUsernameInContext(request, username, jwtToken)
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun validateAuthorizationHeader(authorizationHeader: String?): Boolean {
        return authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)
    }

    private fun addUsernameInContext(request: HttpServletRequest, username: String, jwtToken: String) {
        val userDetails = authenticationService.loadUserByUsername(username)
        if (jwtTokenProvider.validateToken(jwtToken, userDetails)) {
            val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.authorities
            )
            usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
        }
    }
}
