# Profile Configuration

The main configuration file of our application is application.properties file.

In order to create profiles, we can create another properties file:
- application-PROFILENAME.properties 
- application-PROFILENAME.yml 

In our main application.properties, we can set our default profile using the following property:
```properties
spring.profiles.active=PROFILENAME
```

A few examples of profile:
- application-local.yaml
```yaml
logging:
  level:
    com.jlima: DEBUG
```

- application-prod.yaml
```yaml
logging:
  level:
    com.jlima: INFO
```

Running with maven
```shell
 mvn spring-boot:run -Dspring-boot.run.profiles=devl
```

Running with gradle

```shell
gradle bootRun -Dspring.profiles.active=devl

#OR

SPRING_PROFILES_ACTIVE=devl gradle clean bootRun
```

## Bonus
On the code we can get through constructor, through the @Profile("profileName") annotation or get the environment
variables using @Value("\${profileName}")

```kotlin
@Profile("profileName") // first way to get the profile
class SomeClass(private val environment: Environment) { // second way to get the profile
    @Value("\${spring.profiles.active}") // third way to get the profile
    lateinit val profileName;

    fun someMethod() {
        if (this.environment.activeProfile.contains("profileName")) {
            //do something using the second way to get the profile
        };
    }

    @value("")
    fun otherMethod() {

    }
}
```