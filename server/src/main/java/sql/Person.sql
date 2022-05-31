

CREATE TABLE IF NOT EXISTS Person
(
    id          SERIAL PRIMARY KEY UNIQUE,
    date        date                            NOT NUll,                    --Поле не может быть null
    name        VARCHAR(80)                     NOT NULL,--Поле не может быть null, Строка не может быть пустой
    coordinates INT REFERENCES Coordinates (id) NOT NULL,                    --Поле не может быть null
    height      Int                             NOT NULL,--Поле не может быть null, Значение поля должно быть больше 0
    weight      float                           NOT NULL Check (weight > 0), --Значение поля должно быть больше 0
    eyesColor   eyesColor                       NOT NUll,                    --Поле не может быть null
    hairsColor  hairsColor                      NOT NUll,                    --Поле не может быть null
    location    INT REFERENCES Location (id)    NOT NUll                     --Поле не может быть null
);

--     TABLESPACE pg_default;

ALTER TABLE IF EXISTS Person
    OWNER to postgres;

INSERT INTO Location(x, y, z, name)
VALUES (10, 10, 10.00, 'moscow');

INSERT INTO Location(x, y, z, name)
VALUES (10, 10, 10.00, 'Peru');

INSERT INTO Coordinates(x, y)
VALUES (10, 10.05);


INSERT INTO Person(date, name, coordinates, height, weight, eyesColor, hairsColor, location)
VALUES ('2021-11-29', 'name', 1, 100, 100, 'GREEN', 'BLACK', 1);
INSERT INTO Person(date, name, coordinates, height, weight, eyesColor, hairsColor, location)
VALUES ('2021-11-29', 'name', 1, 100, 100, 'GREEN', 'BLACK', 2);

SELECT * FROM Location;

SELECT * FROM Person;

SELECT * FROM Person, Location WHERE Person.location = 2;

SELECT * FROM Person JOIN Location On Person.location = Location WHERE Person.location = 2

-- SELECT *
-- FROM tbPeoples, tbPosition
-- WHERE tbPeoples.idPosition=tbPosition.idPosition


-- SELECT
-- FROM Person WHERE  ;

-- SELECT
--     tc.table_schema,
--     tc.constraint_name,
--     tc.table_name,
--     kcu.column_name,
--     ccu.table_schema AS foreign_table_schema,
--     ccu.table_name AS foreign_table_name,
--     ccu.column_name AS foreign_column_name
-- FROM
--     information_schema.table_constraints AS tc
--         JOIN information_schema.key_column_usage AS kcu
--              ON tc.constraint_name = kcu.constraint_name
--                  AND tc.table_schema = kcu.table_schema
--         JOIN information_schema.constraint_column_usage AS ccu
--              ON ccu.constraint_name = tc.constraint_name
--                  AND ccu.table_schema = tc.table_schema
-- WHERE tc.constraint_type = 'FOREIGN KEY' AND tc.table_name='person';
-- WHERE person.id = coordinates.id = location.id;
-- VALUES ('2021-11-29', 'Oslo', 54, 37);
--
-- INSERT INTO weather VALUES ('Oslo', 46, 50, 0.25, '2021-11-27');