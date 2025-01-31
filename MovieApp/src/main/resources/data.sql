-- Create the users table if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL
);

-- Create the user_roles table if it doesn't exist
CREATE TABLE IF NOT EXISTS user_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,  -- Changed to BIGINT to match the 'id' field in 'users'
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create the movies table if it doesn't exist
CREATE TABLE IF NOT EXISTS movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    director VARCHAR(255),
    release_year INT,
    genre VARCHAR(255),
    imdb_rating DOUBLE
);

-- Create the watchlist table if it doesn't exist
CREATE TABLE IF NOT EXISTS watchlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,  -- Changed to BIGINT to match the 'id' field in 'users'
    movie_id BIGINT NOT NULL,  -- Changed to BIGINT to match the 'id' field in 'movies'
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    UNIQUE (user_id, movie_id)  -- To prevent duplicates
);

-- Insert demo data into the users table
INSERT INTO users (username, email, password, enabled) 
VALUES 
    ('john', 'john@example.com', '{noop}password123', TRUE),
    ('jane', 'jane@example.com', '{noop}password456', TRUE),
    ('alice', 'alice@example.com', '{noop}password789', TRUE);

-- Insert demo data into the user_roles table
INSERT INTO user_roles (user_id, role) 
VALUES
    (1, 'ROLE_ADMIN'),
    (1, 'ROLE_USER'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_USER');

-- Insert demo data into the movies table
INSERT INTO movies (title, director, release_year, genre, imdb_rating)
VALUES
    ('The Matrix', 'Lana Wachowski, Lilly Wachowski', 1999, 'Sci-Fi', 8.7),
    ('Inception', 'Christopher Nolan', 2010, 'Sci-Fi', 8.8),
    ('The Dark Knight', 'Christopher Nolan', 2008, 'Action', 9.0);

-- Insert demo data into the watchlist table
INSERT INTO watchlist (user_id, movie_id) 
VALUES
    (1, 1),  -- John Doe's watchlist: The Matrix
    (1, 2),  -- John Doe's watchlist: Inception
    (2, 3),  -- Jane Doe's watchlist: The Dark Knight
    (3, 1);  -- Alice Smith's watchlist: The Matrix
