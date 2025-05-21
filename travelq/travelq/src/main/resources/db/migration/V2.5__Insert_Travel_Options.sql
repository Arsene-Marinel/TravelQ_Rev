-- Insert travel options for each ticket
INSERT INTO travel_options (ticket_id, check_in, seat_selection, extra_baggage)
VALUES 
(1, TRUE, 'BUSINESS', 'CHECKED_BAGGAGE'),        -- John Doe's first ticket with business class and checked baggage
(2, TRUE, 'BUSINESS', 'OVERWEIGHT_BAGGAGE'),     -- John Doe's second ticket with business class and overweight baggage
(3, TRUE, 'BUSINESS', 'CARRIED_BAGGAGE'),        -- John Doe's third ticket with business class and carried baggage
(4, TRUE, 'ECONOMY', 'CHECKED_BAGGAGE'),         -- Jane Smith's first ticket with economy class and checked baggage
(5, TRUE, 'ECONOMY', 'CARRIED_BAGGAGE'),         -- Jane Smith's second ticket with economy class and carried baggage
(6, TRUE, 'BUSINESS', 'CHECKED_BAGGAGE'),        -- Alice Johnson's first ticket with business class and checked baggage
(7, TRUE, 'BUSINESS', 'SPECIAL_BAGGAGE'),        -- Alice Johnson's second ticket with business class and special baggage
(8, TRUE, 'ECONOMY', 'CARRIED_BAGGAGE'),         -- Alice Johnson's third ticket with economy class and carried baggage
(9, FALSE, 'ECONOMY', 'CHECKED_BAGGAGE'),        -- Bob Wilson's ticket with economy class and checked baggage
(10, TRUE, 'BUSINESS', 'OVERSIZE_BAGGAGE');      -- Emma Brown's ticket with business class and oversize baggage 