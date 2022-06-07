package core;

import dao.CoordinatesDao;
import dao.LocationDao;
import dao.PersonDao;
import dao.UsersDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseManager {
//    LocationDao locationDao = new LocationDao(connection);
//    CoordinatesDao coordinatesDao = new CoordinatesDao(connection);
//    PersonDao personDao = new PersonDao(connection, locationDao, coordinatesDao);
//    UsersDao usersDao = new UsersDao(connection);
//    Authentication auth = new Authentication(usersDao);

    public void connect(){
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

    }


}
