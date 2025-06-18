--liquibase formatted sql
--changeset system:02-insert-initial-users splitStatements:true endDelimiter=;
INSERT INTO users (id, username, password, full_name, role, enabled) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Администратор системы', 'ADMIN', true);
INSERT INTO users (id, username, password, full_name, role, enabled) VALUES
('550e8400-e29b-41d4-a716-446655440002', 'warehouse', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Складской работник', 'WAREHOUSE_MANAGER', true);
INSERT INTO users (id, username, password, full_name, role, enabled) VALUES
('550e8400-e29b-41d4-a716-446655440003', 'user1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Пользователь объекта 1', 'OBJECT_USER', true); 
 