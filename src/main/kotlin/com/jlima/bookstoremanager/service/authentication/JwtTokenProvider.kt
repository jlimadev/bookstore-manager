package com.jlima.bookstoremanager.service.authentication

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Component
class JwtTokenProvider(
    @Value("\${jwt.expiresIn}")
    private val jwtExpiresIn: Long,

    @Value("\${jwt.secret}")
    private val jwtSecret: String,
) {
    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, JvmType.Object> = HashMap()
        val currentTimestamp = Instant.now()
        val issuedAt = Date.from(currentTimestamp)
        val expirationDate = Date.from(currentTimestamp.plusSeconds(jwtExpiresIn))

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(issuedAt)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun parseToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getClaimsFromToken(token).subject
        return username.equals(userDetails.username) && !isTokenExpired(token)
    }

    fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expirationDate = getClaimsFromToken(token).expiration
        return expirationDate.before(Date.from(Instant.now()))
    }
}
