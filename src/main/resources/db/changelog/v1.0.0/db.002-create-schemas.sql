--liquibase formatted sql
--changeset db.create-schemas:1
CREATE SCHEMA IF NOT EXISTS domain;

--rollback drop schema domain cascade;