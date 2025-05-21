-- Add indexes for better performance

-- Index for username lookup
CREATE INDEX idx_users_username ON users(username);

-- Index for email lookup
CREATE INDEX idx_users_email ON users(email);

-- Index for flight origin and destination, which will be frequently queried
CREATE INDEX idx_flights_origin ON flights(origin);
CREATE INDEX idx_flights_destination ON flights(destination);

-- Index for flight departure and arrival times for date-based searches
CREATE INDEX idx_flights_departure_time ON flights(departure_time);
CREATE INDEX idx_flights_arrival_time ON flights(arrival_time);

-- Index for flight price for sorting and filtering
CREATE INDEX idx_flights_price ON flights(price);

-- Index for ticket purchase date
CREATE INDEX idx_tickets_purchase_date ON tickets(purchase_date);

-- Index for notification read status
CREATE INDEX idx_notifications_is_read ON notifications(is_read); 