# Database Configuration

Since we are able to use multiple profiles, we can create a database to development environment and another to prod. We
can have one just to CI part as well.

We are going to manage the database using liquibase.

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
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/postgres}
    username: ${DATABASE_USERNAME:postgres}
    password: ${DATABASE_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
```

## H2

- We are going to use an in-memory database to run the CI Process.


## Liquibase

Liquibase will manage all migrations in our database.
- Automatic creation or rollbacks in our database.
- We have a changelog that contains one or multiple changeset(s).
  - A Changeset describes a set of changes that liquibase will execute within one database transaction.

In our project we need to add the dependency:

```groovy
implementation("org.liquibase:liquibase-core:4.4.0")
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
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/postgres}
    user: ${LIQUIBASE_DATABASE_USERNAME:postgres}
    password: ${LIQUIBASE_DATABASE_PASSWORD:postgres}
```