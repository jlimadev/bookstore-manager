# Bean Validation

In order to validate our data, we can add the bean validation from spring-boot.

First we add the following dependency:

```groovy
implementation("org.springframework.boot:spring-boot-starter-validation")
```
We can add the validations this way*:
```kotlin
data class AuthorDTO(
    val id: String? = null,

    @field:NotNull
    @field:NotEmpty
    @field:Size(min = 3, max = 255)
    val name: String,

    @field:NotNull
    val birthDate: Date
)

```

*This `field` is only for kotlin.
- Kotlin: `@field:NotNull`
- Java: `@NotNull`

**This document is in progress**

## References
- [Baeldung How To](https://www.baeldung.com/spring-boot-bean-validation)
- [Jakarta Bean Validation](https://beanvalidation.org/)
- [Jakarta Docs](https://beanvalidation.org/2.0-jsr380/spec/#builtinconstraints)