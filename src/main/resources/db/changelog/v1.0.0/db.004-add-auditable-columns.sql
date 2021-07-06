--liquibase formatted sql

--changeset db.004-add-auditable-columns:1
ALTER TABLE domain.author
    ADD COLUMN created_by VARCHAR(150) NOT NULL,
    ADD COLUMN created_at TIMESTAMPTZ NOT NULL,
    ADD COLUMN updated_by VARCHAR(150) NOT NULL,
    ADD COLUMN updated_at TIMESTAMPTZ NOT NULL,
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE domain.publisher
    ADD COLUMN created_by VARCHAR(150) NOT NULL,
    ADD COLUMN created_at TIMESTAMPTZ NOT NULL,
    ADD COLUMN updated_by VARCHAR(150) NOT NULL,
    ADD COLUMN updated_at TIMESTAMPTZ NOT NULL,
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE domain."user"
    ADD COLUMN created_by VARCHAR(150) NOT NULL,
    ADD COLUMN created_at TIMESTAMPTZ NOT NULL,
    ADD COLUMN updated_by VARCHAR(150) NOT NULL,
    ADD COLUMN updated_at TIMESTAMPTZ NOT NULL,
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE domain.book
    ADD COLUMN created_by VARCHAR(150) NOT NULL,
    ADD COLUMN created_at TIMESTAMPTZ NOT NULL,
    ADD COLUMN updated_by VARCHAR(150) NOT NULL,
    ADD COLUMN updated_at TIMESTAMPTZ NOT NULL,
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;
