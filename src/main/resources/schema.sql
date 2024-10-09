
CREATE TABLE IF NOT EXISTS User (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    phone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Authority (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    permission VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_authorities (
    user_id BIGINT NOT NULL,
    authority_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (authority_id) REFERENCES authorities(id),
    PRIMARY KEY (user_id, authority_id)
);

CREATE TABLE IF NOT EXISTS authority_permissions (
    authority_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    FOREIGN KEY (authority_id) REFERENCES authorities(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id),
    PRIMARY KEY (authority_id, permission_id)
);
