CREATE TABLE course
(
    id BIGINT PRIMARY KEY CHECK (id >= 0),
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    credits INT NOT NULL CHECK (credits BETWEEN 1 AND 10)
);
