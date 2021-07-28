# JWT - Json Web Token

To generate our tokens we are going to use JWT.

```groovy
implementation("org.springframework.boot:spring-boot-starter-security")
implementation("io.jsonwebtoken:jjwt:0.9.1")
```

We need to create the JWT options and secure the secret:

```yaml
jwt:
  secret: ${JWT_SECRET:bookstoremanager}
  expiresIn: 3600
```

Create the class to handle the token generation, validation and parsing.

```kotlin
@Component
class JwtTokenManager(
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
            .signWith(SignatureAlgorithm.ES512, jwtSecret)
            .compact()
    }

    fun parseToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getAllClaimsFromToken(token).subject
        return username.equals(userDetails.username) && !isTokenExpired(token)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expirationDate = getAllClaimsFromToken(token).expiration
        return expirationDate.before(Date.from(Instant.now()))
    }
}

```