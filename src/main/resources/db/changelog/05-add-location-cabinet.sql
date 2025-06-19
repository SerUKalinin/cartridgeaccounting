--liquibase formatted sql
--changeset ai:20240620-add-location-cabinet
ALTER TABLE locations
    ADD COLUMN cabinet VARCHAR(64); 