CREATE DATABASE IF NOT EXISTS myapp;

USE myapp;

CREATE TABLE IF NOT EXISTS users (
    id UInt32,
    name String,
    created_at DateTime DEFAULT now()
) ENGINE = MergeTree()
ORDER BY id;

CREATE TABLE user_activity (
    event_time DateTime,
    user_id UInt64,
    event_type Enum('page_view' = 1, 'click' = 2, 'form_submit' = 3),
    page_url String,
    session_id UInt64,
    duration Float32 -- Time spent on the page (in seconds)
) ENGINE = MergeTree()
ORDER BY (toYYYYMMDD(event_time), user_id)
PARTITION BY toYYYYMM(event_time);

INSERT INTO user_activity (event_time, user_id, event_type, page_url, session_id, duration)
VALUES
('2023-10-01 10:00:00', 123, 'page_view', '/home', 987654, 120.5),
('2023-10-01 10:01:00', 123, 'click', '/home', 987654, 5.2),
('2023-10-01 10:02:00', 456, 'page_view', '/about', 123456, 90.0);
