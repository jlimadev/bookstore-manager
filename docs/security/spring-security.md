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
