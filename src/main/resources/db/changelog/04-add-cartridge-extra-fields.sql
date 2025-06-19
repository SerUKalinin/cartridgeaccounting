--liquibase formatted sql
--changeset ai:20240619-add-cartridge-extra-fields
ALTER TABLE cartridges
    ADD COLUMN brand VARCHAR(64),
    ADD COLUMN part_number VARCHAR(64),
    ADD COLUMN color VARCHAR(32),
    ADD COLUMN compatible_printers VARCHAR(255); 