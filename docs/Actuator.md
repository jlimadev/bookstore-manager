# Spring Boot Actuator

It is a monitoring tool to spring boot applications. We can get a lot of data from our application, to get insights of
its performance.

By default, the only enable endpoint is /actuator/health and /actuator/info.

We only need to add the following dependency.

```groovy
implementation("org.springframework.boot:spring-boot-starter-actuator")
```

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
    <version>${springboot-version}</version>
</dependency>
```

## Enable endpoints

What can get by using actuator?

- Main endpoints of /actuator: [/info, /health, /env, /beans, /metrics, /shutdown]

To enable or configure our endpoints, we can change in application.properties.

```properties
# management.endpoints.web.exposure.include=* (all endpoints)
management.endpoints.web.exposure.include=auditevents,beans,configprops
management.endpoints.web.exposure.exclude=loggers
# enable shutdown endpoint. accessible through a POST in http://localhost:8080/actuator/shutdown
management.endpoint.shutdown.enabled=true
```

## Create custom endpoints

we can customize it in different ways:

### Creating a properties

- By configurations inside application.properties | .yaml

To access maven properties we can use double at, such as `@project.myProp@` or just a custom property as follows:

```properties
info.app.name=@project.name@
info.app.name.groupId=@project.groupId@
info.app.name.artifactId=@project.artifactId@
info.app.name.version=@project.version@
info.custom.prop=value
info.banana=value
```

If we are using gradle, we can activate a configuration in our build.gradle 
This will expose application data in /info endpoint
```groovy
springBoot {
    buildInfo()
}
```

### By creating configuration classes:

- https://www.baeldung.com/spring-boot-actuators#boot-2x-actuator
- https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.implementing-custom
    
```kotlin
package com.jlima.bookstoremanager.config.actuator

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.stereotype.Component

@Component
@EndpointWebExtension(endpoint = InfoEndpoint::class)
class ActuatorCustomEndpoint(
    private val infoEndpoint: InfoEndpoint,
    private val buildProperties: BuildProperties
) {
    @ReadOperation
    fun info(): WebEndpointResponse<Map<String, Any>> {
        val info: Map<String, Any> = this.infoEndpoint.info()
        val customInfo = HashMap<String, Any>(info)
        customInfo["actuatorConfig"] = "custom configuration from actuator config file"
        return WebEndpointResponse(customInfo, 200)
    }
}
```

## References

- [Spring boot actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Actuator endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#production-ready-endpoints)
- [Custom endpoints - By Spring](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.implementing-custom.web)
- [Baeldung Spring Actuator 2.x](https://www.baeldung.com/spring-boot-actuators#boot-2x-actuator)
- [Custom Actuator Endpoints /info - By Spring](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.implementing-custom)

TODO: [AmigosCode Actuator Youtube Course](https://www.youtube.com/watch?v=LQlypTjmgZM)