# Spring Port and ContextPath

https://www.baeldung.com/spring-boot-context-path

We just need to add the following configuration to our application.yaml (or application.properties)

```yaml
server:
  port: 8080
  servlet:
    context-path: /api/v1
```