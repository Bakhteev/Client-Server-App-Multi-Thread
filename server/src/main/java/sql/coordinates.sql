CREATE TABLE IF NOT EXISTS Coordinates(
        id Serial PRIMARY KEY,
        x integer,
        y integer
);


ALTER TABLE IF EXISTS Coordinates
    OWNER to postgres;

