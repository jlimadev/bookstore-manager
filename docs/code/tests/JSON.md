# Working with JSON (Jackson)

In this project an extension function has been created in order to facilitate JSON generation/conversion.

```groovy
implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
```

```kotlin
fun Any.toJson(): String {
    val mapper = ObjectMapper()
        .registerModule(KotlinModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    return mapper.writeValueAsString(this)
}
```

More docs on Jackson:
- [Jackson Module Kotlin](https://github.com/FasterXML/jackson-module-kotlin)
- [Baeldung - Jackson](https://www.baeldung.com/jackson)
- [Baeldung - ObjectMapper](https://www.baeldung.com/spring-boot-customize-jackson-objectmapper)
- [Baeldung - Jackson Date](https://www.baeldung.com/jackson-serialize-dates)
- [Javadoc Jackson](https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-core/latest/index.html)
