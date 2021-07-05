# Database Configuration

Since we are able to use multiple profiles, we can create a database to development environment and another to prod. We
can have one just to CI part as well.

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

## H2

- We are going to use an in-memory database to run the CI Process.
