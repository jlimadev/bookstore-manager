--liquibase formatted sql
--changeset db.create-domain.author:1

CREATE TABLE IF NOT EXISTS domain.author(
    id          uuid DEFAULT uuid_generate_v4(),
    birth_date  date,
    primary key (id)
);