--liquibase formatted sql
--changeset db.create-tables:1

CREATE TABLE IF NOT EXISTS domain.author(
    id          UUID DEFAULT uuid_generate_v4(),
    name        VARCHAR(255),
    birth_date  DATE,
    primary key (id)
);

--changeset db.create-tables:2
CREATE TABLE IF NOT EXISTS domain.publisher(
    id                  UUID DEFAULT uuid_generate_v4(),
    name                VARCHAR(255),
    code                VARCHAR(100),
    foundation_date     DATE,
    PRIMARY KEY (id)
);

--changeset db.create-tables:3
CREATE TABLE IF NOT EXISTS domain.user(
    id          UUID DEFAULT uuid_generate_v4(),
    name        VARCHAR(255),
    gender      VARCHAR(6),
    birth_date  DATE,
    email       VARCHAR(100),
    password    VARCHAR(100),
    role        VARCHAR(100),
    PRIMARY KEY (id)
);

--changeset db.create-tables:4
CREATE TABLE IF NOT EXISTS domain.book(
    id              UUID DEFAULT uuid_generate_v4(),
    name            VARCHAR(255),
    isbn            VARCHAR(17),
    pages           INT,
    chapters        INT,
    release_date    DATE,
    author_id       UUID CONSTRAINT author_id_fk REFERENCES domain.author,
    publisher_id    UUID CONSTRAINT publisher_id_fk REFERENCES domain.publisher,
    user_id         UUID CONSTRAINT user_id_fk REFERENCES domain.user,
    PRIMARY KEY (id)
);