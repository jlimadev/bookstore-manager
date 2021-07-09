# Spring Data

We use spring data in order to map the relations to our database. It does all the abstractions for us, map all
transactions, create persistence manager and entity manager.

- [Here](../ops/database.md) we have all the configuration to database in this project.

## Interfaces

We just need to implement one of these interfaces and spring will handle the transaction.

- JpaRepository (✔ in this project)
- CrudRepository
- PagingAndSortingRepository

## Implementation

Install Dependencies (check `build.gradle` file):

```groovy
plugins {
    id 'org.springframework.boot' version "$springBootVersion"
    id 'io.spring.dependency-management' version "1.0.11.RELEASE"
    id "org.jetbrains.kotlin.plugin.jpa" version "$kotlinVersion"
}

apply plugin: 'kotlin'
apply plugin: "kotlin-spring"
apply plugin: "kotlin-jpa"

implementation("org.springframework.boot:spring-boot-starter-data-jpa")
implementation("org.liquibase:liquibase-core:4.4.0")
runtimeOnly("com.h2database:h2:1.4.200")
runtimeOnly("org.postgresql:postgresql:42.2.22")
```

## Repository creation

In order to create a repository we need to add an interface that implements the JpaRepository and map the Entity (with the database relationships) and the id type:

````kotlin
interface UserRepository : JpaRepository<AuthorEntity, UUID>
````

We can inject the repository, because spring creates a bean to us.

This is entity has to be mapped to the database:

```kotlin
@Entity
@Table(schema = "domain", name = "author")
data class AuthorEntity(
    @Column(name = "name", length = 255)
    var name: String,

    @Column(name = "birth_date")
    @Temporal(TemporalType.TIMESTAMP)
    var birthDate: Date,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    val books: List<BookEntity>,
) : AuditableEntity()

```

## References
- [Spring official docs](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference)
- [Maven example](https://mkyong.com/spring-boot/spring-boot-spring-data-jpa/)
- [Spring Data JPA Interview Questions and Answers](https://www.netsurfingzone.com/jpa/spring-data-jpa-interview-questions-and-answers/#What_is_the_hierarchy_of_repository_interfaces/classes_in_Spring_Data_JPA)
