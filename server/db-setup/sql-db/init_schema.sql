CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE posts (
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