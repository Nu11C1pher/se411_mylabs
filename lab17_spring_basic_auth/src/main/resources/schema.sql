drop table if exists product;

CREATE table product (
 id int not null auto_increment PRIMARY KEY,
 name varchar(100) not null,
 description varchar(100),
 price DECIMAL(8, 2)
);

CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL
);
