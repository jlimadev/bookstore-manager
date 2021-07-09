# Bookstore Manager
[![Build Status](https://travis-ci.com/jlimadev/bookstore-manager.svg?branch=master)](https://travis-ci.com/jlimadev/bookstore-manager)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jlimadev_bookstore-manager&metric=alert_status)](https://sonarcloud.io/dashboard?id=jlimadev_bookstore-manager)

This Bookstore Manager project purpose is to deliver a REST API to a bookstore.

- This project has been developed with Springboot and Kotlin. The package manager is Gradle.
- This project uses TravisCI and Heroku for CI/CD
- This project has CI Integrated code analysis with SonarCloud

## Running the project

You can run the project by using the maven command

```shell
gradle bootRun
# OR
./gradlew bootRun
```

## Development Process

- Setup project with Spring Initializr
- Application Configurations
  - [x] Add `springfox` Swagger UI (open-api v2 and v3) - [Springfox](docs/openapi/SpringfoxConfiguration.md)
  - [x] Add `springdoc-openapi-ui` (open-api v3) [Optional, this is other provider] - [Springdoc](docs/openapi/SpringdocConfiguration.md)
  - [x] Configure [Profiles](docs/ops/profiles.md)
  - [x] Configure [Actuator](docs/ops/actuator.md)
- CI/CD:
  - [x] Configure [TravisCI](docs/ops/continuous-integration-delivery.md)
  - [x] Configure [SonarCloud with TravisCI](docs/ops/quality.md)
  - [x] Add [KTLint and Jacoco](docs/ops/quality.md)
- [Database](docs/ops/database.md):
  - [x] Add Postgres database local (with docker-compose)
  - [x] Add Postgres database prod (with heroku)
  - [x] Add Liquibase to handle migrations
  - [x] Add H2 and Profile to CI process (no postgres and no liquibase)
- Application:
  - [x] Enable [Auditable Entities](docs/security/auditable-classes.md)
  - [x] Create [Global Exception Handler](docs/code/global-exception-handler.md)
  - [x] Configure [Bean Validation](docs/code/bean-validation.md) (spring-boot-starter-validation)
  - [x] Configure DTOs Dependency [MapStruct](docs/code/DTO.md) 
  - [x] Create [Jpa Repositories](docs/code/spring-data-jpa.md)
  - [ ] Create Service Layer
  - [ ] Pagination and Sorting
  - [ ] Create Controllers
  - [ ] Add Cache
  - [ ] Create a docker container of this application

### Database Model

Database [configuration and explanation](docs/ops/database.md)

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

### References

- [What is REST](https://www.codecademy.com/articles/what-is-rest)
- [REST and RESTFUL](https://becode.com.br/o-que-e-api-rest-e-restful/)
- [HTTP status codes](https://restfulapi.net/http-status-codes/)
- [RESTFUL status codes and practices](https://www.restapitutorial.com/lessons/httpmethods.html#:~:text=The%20primary%20or%20most%2Dcommonly,or%20CRUD)%20operations%2C%20respectively.)
- [Springboot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/index.html)
- [Springboot Starters](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using.build-systems.starters)