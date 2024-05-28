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
    name        VARCHAR(512)                                   NOT NULL,
    description VARCHAR(512),
    type        VARCHAR(128)                                   NOT NULL,
    price       INT                                            NOT NULL CHECK (price >= 0),
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

CREATE TABLE IF NOT EXISTS review
(
    id          BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES users (id) ON DELETE SET DEFAULT NOT NULL DEFAULT -1,
    product_id  BIGINT REFERENCES product (id) ON DELETE CASCADE   NOT NULL,
    description VARCHAR(512)                                       NOT NULL,
    rating      INT                                                NOT NULL CHECK (rating >= 1 AND rating <= 5)
);

CREATE INDEX for_search ON product (name);