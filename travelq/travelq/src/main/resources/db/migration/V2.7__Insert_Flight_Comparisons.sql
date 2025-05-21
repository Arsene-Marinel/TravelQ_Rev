-- Insert flight comparisons
INSERT INTO flight_comparisons (created_at)
VALUES 
('2025-05-05 10:15:00'),
('2025-05-06 11:30:00'),
('2025-05-07 14:45:00'),
('2025-05-08 16:20:00'),
('2025-05-09 09:10:00');

-- Insert flight comparison items (connecting flights to comparisons)
INSERT INTO flight_comparison_items (comparison_id, flight_id)
VALUES 
-- First comparison comparing New York-London with London-Paris
(1, 1), 
(1, 2),

-- Second comparison comparing Paris-Rome with Rome-Barcelona
(2, 3),
(2, 4),

-- Third comparison comparing Barcelona-Madrid with Madrid-Lisbon
(3, 5),
(3, 6),

-- Fourth comparison comparing Lisbon-Amsterdam with Amsterdam-Berlin
(4, 7),
(4, 8),

-- Fifth comparison comparing Berlin-Prague with Prague-Vienna
(5, 9),
(5, 10); 