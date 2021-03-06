# OpenAPI v3.0 with Springdoc Configurations
We need first install `springdoc-openapi-ui` (and for kotlin `springdoc-openapi-kotlin`).

```xml
<properties>
    <springdoc-version>1.5.9</springdoc-version>
</properties>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>${springdoc-version}</version>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-kotlin</artifactId>
    <version>${springdoc-version}</version>
</dependency>
```

```groovy
implementation("org.springdoc:springdoc-openapi-ui:${springdocVersion}")
    implementation("org.springdoc:springdoc-openapi-kotlin:${springdocVersion}")
```

We can also, create the configurations to the api, in a configuration file

```kotlin
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(
        title = "Bookstore Manager API",
        description = "Bookstore Manager Project",
        version = "1.0.0",
        contact = Contact(name = "Jonathan Lima", url = "https://github.com/jlimadev", email = "jlima.dev@gmail.com")
    ),
)
class SpringdocConfig {
}
```

After that, we can access `http://localhost:8080/v3/api-docs/`.

We can change the path on application.properties file, to be something like:
```properties
springdoc.swagger-ui.path=/springdoc/swagger-ui
springdoc.api-docs.path=/springdoc/v3/api-docs
# access ui now from http://localhost:8080/springdoc/swagger-ui
# access openapi now from http://localhost:8080/springdoc/v3/api-docs
```

Finally, in order to scan the right packages, we can set it, in our application.properties as well
```properties
springdoc.packagesToScan=com.jlima.bookstoremanager
```

```kotlin
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@RestController
@RequestMapping("/books")
class BookController {
    @Operation(summary = "This method returns a hello world")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "Success method return")])
    @GetMapping
    fun hello(): ResponseEntity<String> {
        return ResponseEntity("Hello world!", HttpStatus.OK)
    }
}
```

References:
- https://www.baeldung.com/spring-rest-openapi-documentation
- https://reflectoring.io/spring-boot-springdoc/
- https://springdoc.org/

All Spring docs we can use:
- org.springdoc:springdoc-openapi-ui:$springDocOpenApiVersion
- org.springdoc:springdoc-openapi-security:$springDocOpenApiVersion
- org.springdoc:springdoc-openapi-kotlin:$springDocOpenApiVersion
- org.springdoc:springdoc-openapi-data-rest:$springDocOpenApiVersion
- org.springdoc:springdoc-openapi-webflux-ui:$springDocOpenApiVersion
- org.springdoc:springdoc-openapi-hateoas:$springDocOpenApiVersion
- org.springdoc:springdoc-openapi-groovy:$springDocOpenApiVersion