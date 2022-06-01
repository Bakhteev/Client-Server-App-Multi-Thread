import commands.*;
import dao.CoordinatesDao;
import dao.LocationDao;
import dao.PersonDao;
import dto.PersonDto;
import managers.LinkedListCollectionManager;
import managers.ServerCommandManager;
import models.*;
import org.postgresql.util.PSQLException;
import utils.FileWorker;
import utils.JSONParser;

import java.io.Console;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        FileWorker fileWorker;
//        try {
//            fileWorker = new FileWorker(args[1]);
//        } catch (ArrayIndexOutOfBoundsException e) {
//            fileWorker = new FileWorker("./data.json");
//        }
//        JSONParser jsonParser = new JSONParser();
//        LinkedListCollectionManager collectionManager = new LinkedListCollectionManager();
//        collectionManager.loadCollection((jsonParser.JSONParse(fileWorker.readFile(), Person[].class)));
//
//        ServerCommandManager commandManager = new ServerCommandManager();
//        commandManager.addCommands(new AbstractCommand[]{
//                new HelpCommand(commandManager),
//                new InfoCommand(collectionManager),
//                new ShowCommand(collectionManager.getCollection()),
//                new AddCommand(collectionManager),
//                new UpdateCommand(collectionManager),
//                new RemoveByIdCommand(collectionManager),
//                new AddIfMinCommand(collectionManager),
//                new ExitCommand(),
//                new ExecuteScriptCommand(),
//                new ClearCommand(collectionManager),
//                new RemoveGreaterCommand(collectionManager),
//                new PrintDescendingCommand(collectionManager.getCollection()),
//                new PrintUniqueLocationCommand(collectionManager.getCollection()),
//                new CountByHeightCommand(collectionManager.getCollection()),
//                new RemoveFirstCommand(collectionManager),
//        });
//
//        FileWorker finalFileWorker = fileWorker;
//
//
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            public void run() {
//                try {
//                    finalFileWorker.saveFile(JSONParser.toJSON(collectionManager.getCollection()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    Console console = System.console();
//                    String input = null;
//                    try {
//                        input = console.readLine().trim();
//                        if (input.equalsIgnoreCase("save")) {
//                            new SaveCommand(collectionManager, finalFileWorker).execute(null);
//                        }
//                        if (input.equalsIgnoreCase("exit")) {
//                            new ServerExitCommand(collectionManager, finalFileWorker).execute(null);
//                        }
//                    } catch (NullPointerException e) {
//                        System.out.println("\u001B[31mDon't do this!!! Better use exit command\u001B[0m");
//                        System.out.println("\u001B[32mServer is closing\u001B[0m");
//                    }
//                }
//            }
//        }).start();
//
//
//        Server server = new Server(Integer.parseInt(args[0]), commandManager);
//        server.start();
//        server.connect();

        String url = "jdbc:postgresql://localhost:5432/lab7";
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, "postgres", "323694m");
            System.out.println("Connection OK");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Connection ERROR");
        }

        assert connection != null;
        String sqlReqInit =
//                "BEGIN;\n" +
//                        "\n" +
//                        "DO $$\n" +
//                        "BEGIN\n" +
//                        "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'eyesColor') THEN" +
//                        "        create type eyesColor AS ENUM ('GREEN', 'BLUE', 'ORANGE');" +
//                        "    END IF;\n" +
//                        "END\n" +
//                        "$$;" +
//                        "DO $$\n" +
//                        "BEGIN\n" +
//                        "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'hairsColor') THEN\n" +
//                        "        CREATE TYPE hairsColor AS ENUM ('BLACK', 'WHITE', 'BROWN');" +
//                        "    END IF;\n" +
//                        "END\n" +
//                        "$$;" +
//                        "COMMIT;"+
                        "CREATE TYPE eyesColor AS ENUM ('GREEN', 'BLUE', 'ORANGE');" +
                        "CREATE TYPE hairsColor AS ENUM ('BLACK', 'WHITE', 'BROWN');" +
                        "CREATE TABLE IF NOT EXISTS Coordinates(" +
                        "        id Serial PRIMARY KEY," +
                        "        x integer," +
                        "        y integer" +
                        ");" +
                        "CREATE TABLE IF NOT EXISTS Locations" +
                        "(" +
                        "    id   Serial Unique Primary key," +
                        "    x    integer      NOT NULL, --Поле не может быть null\n" +
                        "    y    integer      NOT NULL," +
                        "    z    float        NOT NULL, --Поле не может быть null\n" +
                        "    name varchar(255) NOT NULL  --Поле не может быть null\n" +
                        ");" +
                        "CREATE TABLE IF NOT EXISTS Users(" +
                        "id SERIAL PRIMARY KEY UNIQUE," +
                        "login VARCHAR(255) NOT NULL UNIQUE," +
                        "password VARCHAR(255) NOT NULL" +
                        ");" +
                        "CREATE TABLE IF NOT EXISTS Persons" +
                        "(" +
                        "    id          SERIAL PRIMARY KEY UNIQUE," +
                        "    date        date                            NOT NUll,                    --Поле не может быть null\n" +
                        "    name        VARCHAR(80)                     NOT NULL,--Поле не может быть null, Строка не может быть пустой\n" +
                        "    coordinates INT REFERENCES Coordinates (id) ON DELETE CASCADE NOT NULL,                    --Поле не может быть null\n" +
                        "    height      Int                             NOT NULL,--Поле не может быть null, Значение поля должно быть больше 0\n" +
                        "    weight      float                           NOT NULL Check (weight > 0), --Значение поля должно быть больше 0\n" +
                        "    eyesColor   eyesColor                       NOT NUll,                    --Поле не может быть null\n" +
                        "    hairsColor  hairsColor                      NOT NUll,                    --Поле не может быть null\n" +
                        "    location    INT REFERENCES Locations (id)   ON DELETE CASCADE   NOT NUll," +
                        "    ownerId INT REFERENCES Users (id) NOT NULL" +
                        ");" +
                        "INSERT INTO Users (login, password)" +
                        "VALUES ('test@mail.ru', 'password');" +
                        "INSERT INTO Locations(x, y, z, name)" +
                        "VALUES " +
                        "(10, 10, 10.00, 'moscow')," +
                        "(10, 10, 10.00, 'moscow');" +
                        "INSERT INTO Coordinates(x, y)" +
                        "VALUES" +
                        "(10, 10.05)," +
                        "(10, 10.05);" +
                        "INSERT INTO Persons(date, name, coordinates, height, weight, eyesColor, hairsColor, location, ownerId)" +
                        "VALUES" +
                        "('2021-11-29', 'name', 1, 100, 100, 'GREEN', 'BLACK', 1, 1)," +
                        "('2021-11-29', 'name', 2, 100, 100, 'GREEN', 'BLACK', 2, 1);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlReqInit);
            preparedStatement.executeUpdate();
        } catch (PSQLException e) {
//            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocationDao locationDao = new LocationDao(connection);
        CoordinatesDao coordinatesDao = new CoordinatesDao(connection);
        PersonDao personDao = new PersonDao(connection, locationDao, coordinatesDao);

        List<Person> arr = personDao.getAll();

//        personDao.getById("1");

//        arr.forEach(System.out::println);
        PersonDto dto = new PersonDto();
        dto.setHeight(156L);
        dto.setName("xui");
        dto.setOwnerId(1);
        dto.setHairsColor(HairsColor.BROWN);
        System.out.println(personDao.getById("1"));
        System.out.println(personDao.update("1", dto));
        System.out.println(personDao.getById("1"));
    }
}
