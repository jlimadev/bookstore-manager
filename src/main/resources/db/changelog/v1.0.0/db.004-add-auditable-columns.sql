--liquibase formatted sql

--changeset db.004-add-auditable-columns:1
ALTER TABLE domain.author
    ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    ADD COLUMN updated_at TIMESTAMPTZ DEFAULT now(),
    ADD COLUMN deleted_at TIMESTAMPTZ DEFAULT now();

ALTER TABLE domain.publisher
    ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    ADD COLUMN updated_at TIMESTAMPTZ DEFAULT now(),
    ADD COLUMN deleted_at TIMESTAMPTZ DEFAULT now();

ALTER TABLE domain."user"
    ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    ADD COLUMN updated_at TIMESTAMPTZ DEFAULT now(),
    ADD COLUMN deleted_at TIMESTAMPTZ DEFAULT now();

ALTER TABLE domain.book
    ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    ADD COLUMN updated_at TIMESTAMPTZ DEFAULT now(),
    ADD COLUMN deleted_at TIMESTAMPTZ DEFAULT now();