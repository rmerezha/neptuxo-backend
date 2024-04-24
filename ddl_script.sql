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
    created_by  BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    description VARCHAR(512),
    type        VARCHAR(128)                                   NOT NULL,
    count       INT                                            NOT NULL CHECK (count >= 0),
    created_at  TIMESTAMP                                      NOT NULL,
    image_path  VARCHAR(256)                                   NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS orders
(
    id          VARCHAR(128) PRIMARY KEY,
    product_id  BIGINT REFERENCES product (id) ON DELETE CASCADE NOT NULL,
    customer_id BIGINT REFERENCES users (id) ON DELETE CASCADE   NOT NULL,
    address     VARCHAR(256)                                     NOT NULL,
    status      VARCHAR(128)                                     NOT NULL,
    created_at  TIMESTAMP                                        NOT NULL
);
