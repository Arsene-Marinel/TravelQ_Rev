-- Insert test tickets
INSERT INTO tickets (user_id, flight_id, purchase_date)
VALUES 
(1, 1, '2025-05-10 09:30:00'),  -- John Doe purchased New York to London flight
(1, 2, '2025-05-10 09:45:00'),  -- John Doe purchased London to Paris flight
(1, 3, '2025-05-10 10:00:00'),  -- John Doe purchased Paris to Rome flight
(2, 4, '2025-05-11 14:20:00'),  -- Jane Smith purchased Rome to Barcelona flight
(2, 5, '2025-05-11 14:35:00'),  -- Jane Smith purchased Barcelona to Madrid flight
(3, 6, '2025-05-12 11:05:00'),  -- Alice Johnson purchased Madrid to Lisbon flight
(3, 7, '2025-05-12 11:15:00'),  -- Alice Johnson purchased Lisbon to Amsterdam flight
(3, 8, '2025-05-12 11:30:00'),  -- Alice Johnson purchased Amsterdam to Berlin flight
(4, 9, '2025-05-13 16:40:00'),  -- Bob Wilson purchased Berlin to Prague flight
(5, 10, '2025-05-14 08:25:00'); -- Emma Brown purchased Prague to Vienna flight 