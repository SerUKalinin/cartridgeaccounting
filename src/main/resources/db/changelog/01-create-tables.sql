--liquibase formatted sql
--changeset system:01-create-tables splitStatements:true endDelimiter=;
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT true
);

CREATE TABLE IF NOT EXISTS locations (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    contact_person VARCHAR(100),
    contact_phone VARCHAR(20),
    description TEXT,
    active BOOLEAN DEFAULT true
);

CREATE TABLE IF NOT EXISTS cartridges (
    id UUID PRIMARY KEY,
    model VARCHAR(100) NOT NULL,
    serial_number VARCHAR(100) UNIQUE,
    resource_pages INTEGER,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'IN_STOCK',
    current_location_id UUID REFERENCES locations(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS operations (
    id UUID PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    count INTEGER NOT NULL DEFAULT 1,
    cartridge_id UUID NOT NULL REFERENCES cartridges(id),
    location_id UUID REFERENCES locations(id),
    performed_by UUID NOT NULL REFERENCES users(id),
    operation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

CREATE INDEX IF NOT EXISTS idx_cartridges_serial_number ON cartridges(serial_number);
CREATE INDEX IF NOT EXISTS idx_cartridges_status ON cartridges(status);
CREATE INDEX IF NOT EXISTS idx_cartridges_location ON cartridges(current_location_id);
CREATE INDEX IF NOT EXISTS idx_operations_cartridge ON operations(cartridge_id);
CREATE INDEX IF NOT EXISTS idx_operations_date ON operations(operation_date);
CREATE INDEX IF NOT EXISTS idx_operations_type ON operations(type);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_locations_name ON locations(name);
CREATE INDEX IF NOT EXISTS idx_locations_active ON locations(active); 