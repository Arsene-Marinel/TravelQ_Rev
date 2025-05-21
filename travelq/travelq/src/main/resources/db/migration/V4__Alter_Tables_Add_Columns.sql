-- Add new columns to existing tables

-- Add created_at timestamp to users table
ALTER TABLE users 
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Add updated_at timestamp to users table
ALTER TABLE users
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Add seat number to travel_options table
ALTER TABLE travel_options 
ADD COLUMN seat_number VARCHAR(10);

-- Update existing travel options with random seat numbers
UPDATE travel_options SET seat_number = 
    CASE 
        WHEN seat_selection = 'BUSINESS' THEN CONCAT('B', FLOOR(1 + RAND() * 20))
        ELSE CONCAT(CHAR(65 + FLOOR(RAND() * 6)), FLOOR(1 + RAND() * 30))
    END;

-- Add flight number to flights table
ALTER TABLE flights 
ADD COLUMN flight_number VARCHAR(10);

-- Update existing flights with flight numbers
UPDATE flights SET flight_number = 
    CONCAT(
        CASE 
            WHEN origin = 'New York' THEN 'NY'
            WHEN origin = 'London' THEN 'LN'
            WHEN origin = 'Paris' THEN 'PR'
            WHEN origin = 'Rome' THEN 'RM'
            WHEN origin = 'Barcelona' THEN 'BC'
            WHEN origin = 'Madrid' THEN 'MD'
            WHEN origin = 'Lisbon' THEN 'LB'
            WHEN origin = 'Amsterdam' THEN 'AM'
            WHEN origin = 'Berlin' THEN 'BL'
            WHEN origin = 'Prague' THEN 'PG'
            ELSE 'XX'
        END,
        FLOOR(100 + RAND() * 900)
    ); 