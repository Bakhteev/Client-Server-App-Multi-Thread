package dao;

import dto.PersonDto;
import models.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class PersonDao implements DAO<Person> {
    private final Connection connection;
    private final LocationDao locationDao;
    private final CoordinatesDao coordinatesDao;

    final private String getAllPersonsQuery = "SELECT * FROM Persons";
    final private String getPersonByIdQuery = "SELECT * FROM Persons WHERE id = ?";
    final private String createPersonQuery = "INSERT INTO Persons(date, name, coordinates, height, weight, eyesColor, hairsColor, location, " +
            "ownerId) VALUES (?,?,?,?,?,?,?,?,?)";
    final private String updatePersonQuery = "UPDATE Persons SET name=?, coordinates=?, height=?, weight=?,hairsColor=?,location=? WHERE id =" +
            " ?" +
            " AND ownerId = ?";

    final private String deletePersonByIdQuery = "DELETE FROM Persons WHERE id=? AND ownerId=?";
    final private String deletePersonQuery = "DELETE FROM Persons WHERE ownerId=?";

    public PersonDao(Connection connection, LocationDao locationDao, CoordinatesDao coordinatesDao) {
        this.connection = connection;
        this.coordinatesDao = coordinatesDao;
        this.locationDao = locationDao;
    }

    @Override
    public List<Person> getAll() {
        List<Person> listOfPerson = new LinkedList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();

            ResultSet personsFromDb = statement.executeQuery(getAllPersonsQuery);
            while (personsFromDb.next()) {
                Integer id = personsFromDb.getInt("id");

                String name = personsFromDb.getString("name");
                LocalDateTime dateTime = personsFromDb.getTimestamp("date").toLocalDateTime();
                float weight = personsFromDb.getFloat("weight");
                Long height = personsFromDb.getLong("height");
                Location location = locationDao.getById(personsFromDb.getString("location"));
                Coordinates coordinates = coordinatesDao.getById(personsFromDb.getString("coordinates"));
                HairsColor hairsColor = HairsColor.valueOf(personsFromDb.getString("hairsColor"));
                EyesColor eyesColor = EyesColor.valueOf(personsFromDb.getString("eyesColor"));
                int ownerId = personsFromDb.getInt("ownerId");

                Person person = new Person(id, name, coordinates, dateTime, height, weight, eyesColor, hairsColor, location, ownerId);
                listOfPerson.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfPerson;
    }

    @Override
    public Person getById(String id) {

        return null;
    }

    @Override
    public Person create(PersonDto dto) {
        return null;
    }

    @Override
    public Person update(String id, PersonDto dto) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void delete(Person element) {

    }
}
