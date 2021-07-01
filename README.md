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
- Configure actuator
- Configure TravisCI

### OpenAPI Configurations

we can configure OpenApi/swagger in multiple ways. In this project we have two configuration to it.

- [Springfox](docs/openapi/SpringfoxConfiguration.md)
- [Springdoc](docs/openapi/SpringdocConfiguration.md)

### Profiles

We can use multiple profiles in our application. Here is a document with more explanation.

- [Profiles](docs/Profiles.md)

### Actuator

We can monitor our service by using [Actuator](docs/Actuator.md)

### CI/CD

Following the best practices, we use CI/CD processes in this project. [Read more here](docs/CICD.md). 

For CI, we use TravisCI and for CD, we use Heroku.

### Database Model

<p align="center"> <img src="docs/assets/db-model.jpg" width=590 alt="database"></p>

### Rest Endpoints

- Authors
    - POST [/api/v1/authors]
    - GET (all) [/api/v1/authors]
    - GET (one) [/api/v1/authors/{name}]
    - PUT [/api/v1/authors/{id}]
    - DELETE [/api/v1/authors/{id}]
- Publishers
    - POST [/api/v1/publishers]
    - GET (all) [/api/v1/publishers]
    - GET (one) [/api/v1/publishers/{id}]
    - PUT [/api/v1/publishers/{id}]
    - DELETE [/api/v1/publishers/{id}]
- Users
    - POST [/api/v1/users]
    - POST [/authenticate]
    - GET (all) [/api/v1/users]
    - GET (one) [/api/v1/users/{id}]
    - PUT [/api/v1/users/{id}]
    - DELETE [/api/v1/users/{id}]
- Books
    - POST [/api/v1/books]
    - GET (all) [/api/v1/books]
    - GET (one) [/api/v1/books/{id}]
    - PUT [/api/v1/books/{id}]
    - DELETE [/api/v1/books/{id}]

# References

- [What is REST](https://www.codecademy.com/articles/what-is-rest)
- [REST and RESTFUL](https://becode.com.br/o-que-e-api-rest-e-restful/)
- [HTTP status codes](https://restfulapi.net/http-status-codes/)
- [RESTFUL status codes and practices](https://www.restapitutorial.com/lessons/httpmethods.html#:~:text=The%20primary%20or%20most%2Dcommonly,or%20CRUD)%20operations%2C%20respectively.)
- [Springboot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/index.html)
- [Springboot starters](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using.build-systems.starters)