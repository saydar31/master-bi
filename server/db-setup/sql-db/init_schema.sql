CREATE TABLE if not exists users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE if not exists posts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_registrations (
    month VARCHAR(7) PRIMARY KEY,  -- Формат: 'YYYY-MM'
    count INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE user_registrations IS 'Статистика регистраций пользователей по месяцам';
COMMENT ON COLUMN user_registrations.month IS 'Месяц в формате YYYY-MM';
COMMENT ON COLUMN user_registrations.count IS 'Количество регистраций';

INSERT INTO user_registrations (month, count) VALUES
    ('2023-01', 125),
    ('2023-02', 143),
    ('2023-03', 178),
    ('2023-04', 156),
    ('2023-05', 192),
    ('2023-06', 210),
    ('2023-07', 198),
    ('2023-08', 225),
    ('2023-09', 240),
    ('2023-10', 215),
    ('2023-11', 203),
    ('2023-12', 189) ON CONFLICT (month) DO NOTHING;


-- CREATE TABLE if not existss
CREATE TABLE if not exists sales_by_day (
    day_of_week VARCHAR(10) PRIMARY KEY,
    sales_count INTEGER NOT NULL
);

CREATE TABLE if not exists call_metrics (
    hour INTEGER PRIMARY KEY,
    success_percent NUMERIC(5,2) NOT NULL
);

CREATE TABLE if not exists customer_types (
    customer_type VARCHAR(10) PRIMARY KEY,
    percentage NUMERIC(5,2) NOT NULL
);

CREATE TABLE if not exists feedback (
    option VARCHAR(50) PRIMARY KEY,
    count INTEGER NOT NULL
);

-- Insert data for sales_by_day
INSERT INTO sales_by_day (day_of_week, sales_count) VALUES
('Monday', 42),
('Tuesday', 56),
('Wednesday', 38),
('Thursday', 61),
('Friday', 89),
('Saturday', 24),
('Sunday', 15);

-- Insert data for call_metrics
INSERT INTO call_metrics (hour, success_percent) VALUES
(9, 62.5),
(10, 68.3),
(11, 71.2),
(12, 65.7),
(13, 70.1),
(14, 73.8),
(15, 75.4),
(16, 72.9),
(17, 68.7);

-- Insert data for customer_types
INSERT INTO customer_types (customer_type, percentage) VALUES
('B2B', 65.2),
('B2C', 34.8);

-- Insert data for feedback (randomized counts as in original)
INSERT INTO feedback (option, count) VALUES
('quality', 120),
('reliability', 85),
('efficiency', 210),
('affordability', 75),
('friendliness', 180),
('responsiveness', 95),
('professionalism', 150),
('convenience', 110),
('effectiveness', 200),
('clarity', 90),
('timeliness', 160),
('flexibility', 130),
('innovation', 70),
('support', 190),
('user-friendliness', 140);
