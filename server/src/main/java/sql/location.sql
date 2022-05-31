CREATE TABLE IF NOT EXISTS Location
(
    id   Serial Unique Primary key,
    x    integer      NOT NULL, --Поле не может быть null
    y    integer      NOT NULL,
    z    float        NOT NULL, --Поле не может быть null
    name varchar(255) NOT NULL  --Поле не может быть null
);

ALTER TABLE IF EXISTS Location
    OWNER to postgres;

