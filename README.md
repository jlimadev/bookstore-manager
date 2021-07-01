# Bookstore Manager

This Bookstore Manager project purpose is to deliver a REST API to a bookstore.

This project has been developed with Springboot and Kotlin. The package manager is Maven.

## Running the project

You can run the project by using the maven command

```shell
mvn spring-boot:run
```

## Development Process

- Setup project with Spring Initializr
- Add swagger (open-api v3) and configurations
- Add springdoc-openapi-ui (open-api v3) and configurations [Optional, this is other provider]
- Configure Profiles

### OpenAPI Configurations

we can configure OpenApi/swagger in multiple ways. In this project we have two configuration to it.

- [Springfox](docs/openapi/SpringfoxConfiguration.md)
- [Springdoc](docs/openapi/SpringdocConfiguration.md)

### Profiles
We can use multiple profiles in our application. Here is a document with more explanation.
- [Profiles](docs/Profiles.md)

# References

- [What is REST](https://www.codecademy.com/articles/what-is-rest)
- [REST and RESTFUL](https://becode.com.br/o-que-e-api-rest-e-restful/)
- [HTTP status codes](https://restfulapi.net/http-status-codes/)
- [RESTFUL status codes and practices](https://www.restapitutorial.com/lessons/httpmethods.html#:~:text=The%20primary%20or%20most%2Dcommonly,or%20CRUD)%20operations%2C%20respectively.)
- [Springboot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/index.html)
- [Springboot starters](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using.build-systems.starters)