-- Insert notifications for users
INSERT INTO notifications (user_id, message, is_read)
VALUES 
(1, 'Your flight from New York to London has been confirmed. Check-in will open 24 hours before departure.', TRUE),
(1, 'Your flight from London to Paris has been confirmed. Check-in will open 24 hours before departure.', TRUE),
(1, 'Flight delay alert: Your flight from Paris to Rome has been delayed by 30 minutes.', FALSE),
(2, 'Your flight from Rome to Barcelona has been confirmed. Check-in will open 24 hours before departure.', TRUE),
(2, 'Gate change alert: Your flight from Barcelona to Madrid will now depart from Gate B12 instead of Gate B8.', FALSE),
(3, 'Your flight from Madrid to Lisbon has been confirmed. Check-in will open 24 hours before departure.', TRUE),
(3, 'Boarding begins in 30 minutes for your flight from Lisbon to Amsterdam.', FALSE),
(3, 'Your luggage has been checked in successfully for your Amsterdam to Berlin flight.', FALSE),
(4, 'Your flight from Berlin to Prague has been confirmed. Check-in will open 24 hours before departure.', TRUE),
(5, 'Weather alert: Expect slight turbulence during your flight from Prague to Vienna due to forecasted storms.', FALSE); 