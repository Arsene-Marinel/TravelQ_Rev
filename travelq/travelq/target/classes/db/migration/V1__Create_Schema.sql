-- Creating schema for TravelQ application

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL
);

-- Travel History table
CREATE TABLE IF NOT EXISTS travel_histories (
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    flights_count INT NOT NULL DEFAULT 0,
    total_spent DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Flights table
CREATE TABLE IF NOT EXISTS flights (
    flight_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stopovers INT NOT NULL DEFAULT 0
);

-- Tickets table
CREATE TABLE IF NOT EXISTS tickets (
    ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    flight_id BIGINT NOT NULL,
    purchase_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id) ON DELETE CASCADE
);

-- Travel Options table
CREATE TABLE IF NOT EXISTS travel_options (
    option_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT NOT NULL UNIQUE,
    check_in BOOLEAN NOT NULL DEFAULT FALSE,
    seat_selection VARCHAR(50) NOT NULL,
    extra_baggage VARCHAR(50),
    FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id) ON DELETE CASCADE
);

-- CREATE TABLE IF NOT EXISTS travel_options (
--     option_id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     ticket_id BIGINT NOT NULL UNIQUE,
--     check_in BOOLEAN NOT NULL DEFAULT FALSE,
--     seat_selection ENUM(
--                          'ECONOMY',
--                          'BUSINESS'
--                         ) NOT NULL,
--     extra_baggage ENUM(
--                           'OVERWEIGHT_BAGGAGE',
--                           'OVERSIZE_BAGGAGE',
--                           'SPECIAL_BAGGAGE',
--                           'EXCEED_NUMBER_BAGGAGE',
--                           'CHECKED_BAGGAGE',
--                           'CARRIED_BAGGAGE'
--                       ),
--     FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id) ON DELETE CASCADE
--     );

-- Notifications table
CREATE TABLE IF NOT EXISTS notifications (
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Flight Comparisons table
CREATE TABLE IF NOT EXISTS flight_comparisons (
    comparison_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Flight Comparison Items junction table
CREATE TABLE IF NOT EXISTS flight_comparison_items (
    comparison_id BIGINT NOT NULL,
    flight_id BIGINT NOT NULL,
    PRIMARY KEY (comparison_id, flight_id),
    FOREIGN KEY (comparison_id) REFERENCES flight_comparisons(comparison_id) ON DELETE CASCADE,
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id) ON DELETE CASCADE
);
 