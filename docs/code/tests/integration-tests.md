# Integration Tests

- `@ExtendWith(SpringExtension::class)`  integrates the Spring TestContext Framework into JUnit 5's Jupiter programming
  model.
- `@WebMvcTest(AuthorController::class)` Using this annotation will disable full auto-configuration and instead apply
  only configuration relevant to MVC tests

Inside our test class we have an `@Autowired` annotation to mock our Class defined on `@WebMvcTest` annotation. This
Class needs a service, so we need to add this mock as a bean. That's why we have this `@MockBean`.

```kotlin
@ExtendWith(SpringExtension::class)
@WebMvcTest(AuthorController::class)
class AuthorControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authorService: AuthorService

    private data class SUT(
        val defaultDTO: AuthorDTO,
        val entityId: UUID,
    )

    private fun makeSut(): SUT {
        val entityId = UUID.randomUUID()
        val defaultDTO = AuthorDTO(
            name = "Jonathan Lima",
            birthDate = Date.from(Instant.now())
        )

        return SUT(
            defaultDTO = defaultDTO,
            entityId = entityId
        )
    }

    @Test
    fun `It should created an Author and Return 201 (Created) when POST`() {
        // Given [Arrange]
        val (defaultDTO, entityId) = makeSut()
        val expectedResponse = defaultDTO.copy(id = entityId.toString())

        // When [Act]
        whenever(authorService.create(any())).thenReturn(expectedResponse)

        // Then [Assert]
        mockMvc
            .post("/api/v1/authors") {
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
                content = defaultDTO.toJson()
            }.andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(expectedResponse.toJson()) }
            }
    }
}
```
## References
- Official Docs on [JUnit Extensions](https://junit.org/junit5/docs/current/user-guide/#extensions)
- Official Docs
  on [WebMvcTest](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/servlet/WebMvcTest.html)
- MockMvc [Examples](https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/)
- **Baeldung**
    - [MockMVC with Kotlin](https://www.baeldung.com/kotlin/mockmvc-kotlin-dsl)
    - [Baeldung Integration Test](https://www.baeldung.com/integration-testing-in-spring)
    - [Optimizing Spring Integration Test](https://www.baeldung.com/spring-tests)
    - [Spring Boot Testing](https://www.baeldung.com/spring-boot-testing)
    - [Testing Exception with MockMVC](https://www.baeldung.com/spring-mvc-test-exceptions)

## Videos
- Testes unitários em Controllers com Spring MockMVC [AlgaWorks video](https://www.youtube.com/watch?v=ngbKmhXDP4A)
- How to Unit Test & Integration Test REST Controllers [Dan Vega Video](https://www.youtube.com/watch?v=pNiRNRgi5Ws)
