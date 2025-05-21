-- Create views for reporting

-- Create a view that combines user information with their travel history
CREATE OR REPLACE VIEW user_travel_summary AS
SELECT 
    u.user_id,
    u.username,
    u.email,
    u.first_name,
    u.last_name,
    th.flights_count,
    th.total_spent,
    (SELECT COUNT(*) FROM tickets t WHERE t.user_id = u.user_id) AS actual_tickets_count
FROM 
    users u
JOIN 
    travel_histories th ON u.user_id = th.user_id;

-- Create a view for flight details with tickets sold
CREATE OR REPLACE VIEW flight_ticket_summary AS
SELECT 
    f.flight_id,
    f.flight_number,
    f.origin,
    f.destination,
    f.departure_time,
    f.arrival_time,
    f.price,
    f.stopovers,
    COUNT(t.ticket_id) AS tickets_sold,
    SUM(CASE WHEN topt.seat_selection = 'BUSINESS' THEN 1 ELSE 0 END) AS business_class_count,
    SUM(CASE WHEN topt.seat_selection = 'ECONOMY' THEN 1 ELSE 0 END) AS economy_class_count
FROM 
    flights f
LEFT JOIN 
    tickets t ON f.flight_id = t.flight_id
LEFT JOIN 
    travel_options topt ON t.ticket_id = topt.ticket_id
GROUP BY 
    f.flight_id, f.flight_number, f.origin, f.destination, f.departure_time, f.arrival_time, f.price, f.stopovers;

-- Create a view for user notification statistics
CREATE OR REPLACE VIEW user_notification_stats AS
SELECT 
    u.user_id,
    u.username,
    COUNT(n.notification_id) AS total_notifications,
    SUM(CASE WHEN n.is_read = TRUE THEN 1 ELSE 0 END) AS read_notifications,
    SUM(CASE WHEN n.is_read = FALSE THEN 1 ELSE 0 END) AS unread_notifications
FROM 
    users u
LEFT JOIN 
    notifications n ON u.user_id = n.user_id
GROUP BY 
    u.user_id, u.username; 