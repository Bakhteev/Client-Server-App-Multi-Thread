package dao;

import dto.PersonDto;
import models.*;

import java.sql.*;
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
    final private String updatePersonQuery = "UPDATE Persons SET name=?, coordinates=?, height=?, weight=?, hairsColor=CAST(? AS hairsColor)" +
            ", " +
            "location=? " +
            "WHERE id " +
            "=" +
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
        try {
            PreparedStatement statement = connection.prepareStatement(getPersonByIdQuery);
            statement.setInt(1, Integer.parseInt(id));
            ResultSet personFromDb = statement.executeQuery();
            Person person = null;
            while (personFromDb.next()) {
                Integer personsId = personFromDb.getInt("id");
                String name = personFromDb.getString("name");
                LocalDateTime dateTime = personFromDb.getTimestamp("date").toLocalDateTime();
                float weight = personFromDb.getFloat("weight");
                Long height = personFromDb.getLong("height");
                Location location = locationDao.getById(personFromDb.getString("location"));
                Coordinates coordinates = coordinatesDao.getById(personFromDb.getString("coordinates"));
                HairsColor hairsColor = HairsColor.valueOf(personFromDb.getString("hairsColor"));
                EyesColor eyesColor = EyesColor.valueOf(personFromDb.getString("eyesColor"));
                int ownerId = personFromDb.getInt("ownerId");

                person = new Person(personsId, name, coordinates, dateTime, height, weight, eyesColor, hairsColor, location, ownerId);

                break;
            }
            return person;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Person create(PersonDto dto) {

        return null;
    }

    @Override
    public Person update(String id, PersonDto dto) {
        try {
            Person prevPerson = getById(id);
            PreparedStatement statement = connection.prepareStatement(updatePersonQuery);

            statement.setString(1, dto.getName() == null ? prevPerson.getName() : dto.getName());
            statement.setInt(2, dto.getCoordinates() == null ? prevPerson.getCoordinates().getId() : dto.getCoordinates().getId());

            if (dto.getCoordinates() != null) {
                coordinatesDao.update(dto.getCoordinates().getId() + "", dto);
            }

            statement.setLong(3, dto.getHeight() == null ? prevPerson.getHeight() : dto.getHeight());
            statement.setFloat(4, dto.getWeight() == 0 ? prevPerson.getWeight() : dto.getWeight());
            statement.setString(5, dto.getHairsColor() == null ?
                    prevPerson.getHairsColor().name() : dto.getHairsColor().name());
            statement.setInt(6, dto.getLocation() == null ? prevPerson.getLocation().getId() : dto.getLocation().getId());

            if (dto.getLocation() != null) {
                locationDao.update(dto.getLocation().getId() + "", dto);
            }

            statement.setInt(7, Integer.parseInt(id));
            statement.setInt(8, dto.getOwnerId());
            statement.executeUpdate();
            return getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void delete(Person element) {

    }
}
