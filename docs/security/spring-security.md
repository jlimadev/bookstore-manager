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

## References

- Official Docs
  - [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture)
  - [Spring Security Reference](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#prerequisites)
  - [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
- Baeldung
  - List of
    Tutorial [Spring Security Authentication](https://www.baeldung.com/spring-security-authentication-and-registration)
  - [Roles and Privileges](https://www.baeldung.com/role-and-privilege-for-spring-security-registration)
  - [Prevent Brute Force](https://www.baeldung.com/spring-security-block-brute-force-authentication-attempts)
  - [Remember Me](https://www.baeldung.com/spring-security-remember-me)
  - [Spring Security Registration](https://www.baeldung.com/spring-security-registration)
- AmigosCode
  - Spring Security - Free Course on [Youtube](https://www.youtube.com/watch?v=her_7pa0vrg&t=1424s)
  - Spring Security - JWT including Access and Refresh Tokens [Youtube](https://www.youtube.com/watch?v=VVn9OG9nfH0)