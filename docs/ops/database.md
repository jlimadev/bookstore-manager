# Database Configuration

Since we are able to use multiple profiles, we can create a database to development environment and another to prod. We
can have one just to CI part as well.

We are going to manage the database using liquibase.

- Postgres Local [With docker-compose]
- Postgres Prod [On Heroku]
- Liquibase to manage the migrations [to Local and Prod]
- H2 Database for CI process

## Postgres

- We are going to use a local postgres to run in development env.
- We are going to use a cloud database to run in production env.

For local database, we need to create a docker-compose file to download the database with the configurations.

```yaml
version: '3.9'

services:
  postgres:
    image: postgres:13.3
    container_name: bookstore-manager-postgres
    environment:
      POSTGRES_USER: ${POSTGRES_USER:-postgres}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:-postgres}
      PGDATA: /data/postgres
    volumes:
      - postgres:/data/postgres
    networks:
      - bookstore-network
    ports:
      - "5432:5432"
    restart: unless-stopped

networks:
  bookstore-network:
volumes:
  postgres:
```
We can use environment variables alongside to `shell parameter substitution`, this will be something like:

```shell 
${POSTGRES_USER:-postgres}
# If POSTGRES_USER env. variable is not present, the use "postgres" as value
```

Finally, after all the configurations, we can run ``docker-compose up -d``

Now we have to configure the application to connect to the database, in our application.properties (or yaml):

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/postgres}
    username: ${SPRING_DATASOURCE_USERNAME:postgres}
    password: ${SPRING_DATASOURCE_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
```

- About the environment variable names, these given names are related to Heroku variables, if we are using another
  provider, we can give other names.
- Apart from DATABASE_URL, which is always there, Heroku creates 3 environment variables at Runtime. They are:
    - JDBC_DATABASE_URL
    - JDBC_DATABASE_USERNAME
    - JDBC_DATABASE_PASSWORD

In our project we need to add the driver to Postgres and spring data jpa
```groovy
implementation("org.springframework.boot:spring-boot-starter-data-jpa")
runtimeOnly("org.postgresql:postgresql:42.2.22") // check the current version
```

[Stackoverflow Reference](https://stackoverflow.com/a/41020278/13879410)
and [Official Docs](https://devcenter.heroku.com/articles/heroku-postgresql)

## H2

- We are going to use an in-memory database to run the CI Process.

So we need to create a profile to CI, containing the H2 configuration

`application-ci.yaml`

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
  liquibase:
    enabled: false
  jpa:
    hibernate:
      ddl-auto: none
```

Set .travis.yml to use the CI Profile, on building processes, check the [CI-CD docs](./continuous-integration-delivery.md)

```yaml
env:
  - SPRING_PROFILES_ACTIVE=ci
```

In our project we need to add the driver to H2
```groovy
runtimeOnly("com.h2database:h2:1.4.200") // check the current version
```

## Liquibase

Liquibase will manage all migrations in our database.

- Automatic creation or rollbacks in our database.
- We have a changelog that contains one or multiple changeset(s).
    - A Changeset describes a set of changes that liquibase will execute within one database transaction.

In our project we need to add the dependency:

```groovy
implementation("org.liquibase:liquibase-core:4.4.0") // check the current version
```

```xml
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <version>4.4.0</version>
</dependency>
```

Create a master.xml file inside `resources/db` folder:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog/1.9"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog/1.9
                      http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-1.9.xsd">
    <include file="db/changelog/v1.0.0/db.myChangelog1.sql"/>
    <include file="db/changelog/v1.0.0/db.myChangelog2.sql"/>
    <include file="db/changelog/v1.0.0/db.myChangelog3.sql"/>
    <include file="db/changelog/v1.0.0/db.myChangelog4.sql"/>
</databaseChangeLog>
```

Configure our application.properties (or yaml)

```yaml
spring:
  liquibase:
    change-log: classpath:db/master.xml
    url: ${JDBC_DATABASE_URL:jdbc:postgresql://localhost:5432/postgres}
    username: ${JDBC_DATABASE_USERNAME:postgres}
    password: ${JDBC_DATABASE_PASSWORD:postgres}
```