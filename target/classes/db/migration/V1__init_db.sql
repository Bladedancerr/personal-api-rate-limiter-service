CREATE TABLE client (
    id BINARY(16) PRIMARY KEY, -- UUID stored in binary format
    email VARCHAR(100) UNIQUE NOT NULL, -- Email used for login/auth
    password VARCHAR(255) NOT NULL, -- Hashed password (e.g. BCrypt)
    api_key VARCHAR(255) UNIQUE NOT NULL, -- Public-facing token
    rate_limit INT NOT NULL DEFAULT 0, -- Max requests per time window
    user_limit INT NOT NULL DEFAULT 0, -- Max concurrent users
    rate_window_sec BIGINT  NOT NULL DEFAULT 3600, -- Window size in seconds
    enabled BOOLEAN DEFAULT TRUE, -- Account status
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP-- Registration time
);