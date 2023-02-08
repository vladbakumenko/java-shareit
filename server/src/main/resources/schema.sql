CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description  VARCHAR NOT NULL,
    requestor_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    created      TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,
    description  VARCHAR NOT NULL,
    is_available BOOLEAN NOT NULL,
    owner_id     BIGINT REFERENCES users (id) ON DELETE CASCADE,
    request_id   BIGINT REFERENCES requests (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date   TIMESTAMP WITHOUT TIME ZONE,
    end_date     TIMESTAMP WITHOUT TIME ZONE,
    item_id      BIGINT REFERENCES items (id) ON DELETE CASCADE,
    booker_id    BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status       VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text      VARCHAR NOT NULL,
    item_id   BIGINT REFERENCES items (id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    created   TIMESTAMP WITHOUT TIME ZONE
);

DELETE FROM comments;
DELETE FROM bookings;
DELETE FROM items;
DELETE from requests;
DELETE from users;

ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE requests ALTER COLUMN id RESTART WITH 1;
ALTER TABLE items ALTER COLUMN id RESTART WITH 1;
ALTER TABLE bookings ALTER COLUMN id RESTART WITH 1;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 1;