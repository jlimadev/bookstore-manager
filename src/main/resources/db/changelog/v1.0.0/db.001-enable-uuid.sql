--liquibase formatted sql
--changeset db.enable-uuid:1

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
