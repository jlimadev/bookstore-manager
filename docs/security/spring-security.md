# Spring Security

First, we need the following dependencies:

```groovy
implementation("org.springframework.boot:spring-boot-starter-security")
implementation("io.jsonwebtoken:jjwt:0.9.1")
```

## Password Encryption

With these dependencies we can create an instance of encoder, to encrypt data.

```kotlin
@Configuration
class PasswordEncodingConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
```

Since this is a bean, we can inject in any class. In this case our service will receive this bean.

- By doing it, we cannot forget to update our unit tests.

Check `UserService` and `UserServiceTest`.

## User Roles

We can create roles to our users:

- In this project we've added as attribute to user, Role (enum).

## Handle with Authenticated Users

### UserDetails

- We have to create a class to handle the authenticated users and return the user details. This class must implement
  UserDetails from SpringSecurity.
    - It will retrieve basic information about the user, such as Username, Password, Roles, e more methods.
  
```kotlin
package com.jlima.bookstoremanager.dto

class AuthenticatedUser(
    private val username: String,
    private val password: String,
    private val role: String
) : UserDetails {

    companion object {
        private const val ROLE_PREFIX = "ROLE_"
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.singletonList(SimpleGrantedAuthority("$ROLE_PREFIX$role"))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    // other methods
}
```

### Authentication Service

```kotlin
package com.jlima.bookstoremanager.service.authentication

@Service
class AuthenticationService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)
            .orElseThrow { BusinessEntityNotFoundException(AvailableEntities.USER, username) }
        return AuthenticatedUser(
            username = user.email,
            password = user.password,
            role = user.role.name
        )
    }
}
```

### Authentication Entrypoint

```kotlin
package com.jlima.bookstoremanager.config.security

@Component
class JwtAuthenticationEntrypoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }
}
```

## Web Security Configuration

This is the class where we implement our custom way to handle the authentication/security.

- `@EnableWebSecurity` ➡ Allows us to create this custom configuration, so Spring Web Configuration will get the handle
  from here. This class must be `@Configuration`.
- The class must extends `WebSecurityConfigurerAdapter`
- This class will handle:
  - Allow or Deny access to endpoints
  - Define allowed scopes/roles
  - Handle unauthorized
  
Check the implementation here: `com.jlima.bookstoremanager.config.security.WebSecurityConfig`

## References

- Official Docs
    - [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture)
    - [Spring Security Reference](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#prerequisites)
    - [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
- Baeldung
    - List of
      Tutorial [Spring Security Authentication](https://www.baeldung.com/spring-security-authentication-and-registration)
    - How to create [Config for Spring Security](https://www.baeldung.com/java-config-spring-security) ✔
    - [Roles and Privileges](https://www.baeldung.com/role-and-privilege-for-spring-security-registration)
    - [Prevent Brute Force](https://www.baeldung.com/spring-security-block-brute-force-authentication-attempts)
    - [Remember Me](https://www.baeldung.com/spring-security-remember-me)
    - [Spring Security Registration](https://www.baeldung.com/spring-security-registration)
- AmigosCode
    - Spring Security - Free Course on [Youtube](https://www.youtube.com/watch?v=her_7pa0vrg&t=1424s)
    - Spring Security - JWT including Access and Refresh Tokens [Youtube](https://www.youtube.com/watch?v=VVn9OG9nfH0)