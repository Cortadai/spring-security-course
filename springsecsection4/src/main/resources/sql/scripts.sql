CREATE TABLE users (
   username VARCHAR(50) NOT NULL PRIMARY KEY,
   password VARCHAR(500) NOT NULL,
   enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
 username VARCHAR(50) NOT NULL,
 authority VARCHAR(50) NOT NULL,
 CONSTRAINT fk_username FOREIGN KEY(username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

-- Usuario: user / @*AJZVVj5#dwB0 (noop)
INSERT INTO users (username, password, enabled)
VALUES ('user', '{noop}@*AJZVVj5#dwB0', 1);

INSERT INTO authorities (username, authority)
VALUES ('user', 'read');

-- Usuario: admin / D%0c@nc$4wO&4w (bcrypt)
INSERT INTO users (username, password, enabled)
VALUES ('admin', '{bcrypt}$2a$12$PcB3MwQJn6ZG0L4nLAMFMebNqmAGlhmWrxneKqK3jh6Z93beUjehS', 1);

INSERT INTO authorities (username, authority)
VALUES ('admin', 'admin');

CREATE TABLE customers (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   email VARCHAR(45) NOT NULL,
   pwd VARCHAR(200) NOT NULL,
   role VARCHAR(45) NOT NULL
);

-- Usuario: user@example.com / @*AJZVVj5#dwB0 (noop)
INSERT INTO customers (email, pwd, role)
VALUES ('user@example.com', '{noop}@*AJZVVj5#dwB0', 'read');

-- Usuario: admin@example.com / D%0c@nc$4wO&4w (bcrypt)
INSERT INTO customers (email, pwd, role)
VALUES ('admin@example.com', '{bcrypt}$2a$12$PcB3MwQJn6ZG0L4nLAMFMebNqmAGlhmWrxneKqK3jh6Z93beUjehS', 'admin');