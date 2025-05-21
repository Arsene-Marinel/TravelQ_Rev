-- Insert test users
-- Note: passwords are hashed as 'password' using BCrypt
INSERT INTO users (username, password, email, first_name, last_name)
VALUES 
('john_doe', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'john.doe@example.com', 'John', 'Doe'),
('jane_smith', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'jane.smith@example.com', 'Jane', 'Smith'),
('alice_johnson', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'alice.johnson@example.com', 'Alice', 'Johnson'),
('bob_wilson', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'bob.wilson@example.com', 'Bob', 'Wilson'),
('emma_brown', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'emma.brown@example.com', 'Emma', 'Brown'); 