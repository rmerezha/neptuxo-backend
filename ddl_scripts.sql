CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(128) NOT NULL UNIQUE,
    email    VARCHAR(128) NOT NULL UNIQUE,
    passwd   VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS product
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES users (id) NOT NULL,
    description VARCHAR(512),
    type        VARCHAR(128)                 NOT NULL,
    count       INT                          NOT NULL CHECK (count >= 0),
    imagePath   VARCHAR(256)                 NOT NULL UNIQUE

);

CREATE TABLE IF NOT EXISTS preference
(
    user_id BIGINT REFERENCES users (id),
    type    VARCHAR(128),
    PRIMARY KEY (user_id, type)
);

CREATE TABLE IF NOT EXISTS orders
(
    id         VARCHAR(128) PRIMARY KEY,
    product_id BIGINT REFERENCES product (id) NOT NULL,
    user_id    BIGINT REFERENCES users (id)   NOT NULL,
    address    VARCHAR(256)                   NOT NULL,
    type       VARCHAR(128)                   NOT NULL
);
