# Unit Tests

In this project we use:
- JUnit 5
- Mockito
- Springboot

```groovy
testImplementation("org.springframework.boot:spring-boot-starter-test")
testImplementation("org.mockito:mockito-core:3.10.0")
testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
testImplementation "org.mockito:mockito-inline:2.7.21"
testImplementation 'org.jetbrains.kotlin:kotlin-test-junit5'
testImplementation 'org.junit.jupiter:junit-jupiter-api:5.6.0'
testImplementation 'org.assertj:assertj-core:3.11.1'
testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.6.0'
```
## References
- **Baeldung**
    - [JUnit 5 for Kotlin Developers](https://www.baeldung.com/kotlin/junit-5-kotlin)
    - [Mockito With Kotlin](https://www.baeldung.com/kotlin/mockito)
    - [mock() vs @Mock vs @MockBean](https://www.baeldung.com/java-spring-mockito-mock-mockbean)
- Official Page [Mockit Kotlin](https://github.com/mockito/mockito-kotlin)
- Mockit in Kotlin Project [kotlintesting website](https://kotlintesting.com/using-mockito-in-kotlin-projects/)
- Springboot JUnit and Mockito [howtodoinjava](https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockito-junit-example/)
## Videos
- TDD na prática com Java usando @MockBean
  [Michelli Brito](https://www.youtube.com/watch?v=4VmbETu-dcA)