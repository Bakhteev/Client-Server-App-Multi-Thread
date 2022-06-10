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
    private boolean autoCommit = true;

    final private String getAllPersonsQuery = "SELECT * FROM Persons";
    final private String getPersonByIdQuery = "SELECT * FROM Persons WHERE id = ?";
    final private String createPersonQuery = "INSERT INTO Persons(date, name, coordinates, height, weight, eyesColor," +
            "hairsColor, location, ownerId) VALUES (?, ?, ?, ?, ?, CAST(? AS eyesColor), CAST(? AS hairsColor),?, ?) RETURNING id;";
    final private String updatePersonQuery = "UPDATE Persons SET name=?, height=?, weight=?, hairsColor=CAST(? AS hairsColor)" +
            "WHERE id = ? AND ownerId = ?";

    final private String getFirstPersonQuery = "SELECT * FROM Persons Limit 1";

    final private String deletePersonByIdQuery = "DELETE FROM Persons WHERE id=?";
    final private String getAllPersonsByOwnerIdQuery = "SELECT * FROM Persons WHERE ownerId = ?";
    final private String deleteAllPersonsByOwnerIdQuery = "DELETE FROM Persons WHERE ownerId = ?";
    final private String getNumberOfPersonsQuery = "SELECT COUNT(*) FROM Persons;";
    final private String getNumberOfPersonsByOwnerId = "SELECT COUNT(*) FROM Persons WHERE ownerId = ?";

    public PersonDao(Connection connection, LocationDao locationDao, CoordinatesDao coordinatesDao) {
        this.connection = connection;
        this.coordinatesDao = coordinatesDao;
        this.locationDao = locationDao;
    }

    private void setAutoCommit() {
        this.autoCommit = !this.autoCommit;
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Person> getAll() {
        List<Person> listOfPerson = new LinkedList<>();
        try {
            if (autoCommit) {
                setAutoCommit();
            }
            Statement statement = connection.createStatement();

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
            if (!autoCommit) {
                connection.commit();
                setAutoCommit();
            }
            return listOfPerson;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!autoCommit) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return listOfPerson;
        }
    }

    @Override
    public Person getById(String id) {
        try {
            if (autoCommit) {
                setAutoCommit();
            }
            PreparedStatement statement = connection.prepareStatement(getPersonByIdQuery);
            statement.setInt(1, Integer.parseInt(id));
            ResultSet personFromDb = statement.executeQuery();
            if (!personFromDb.next()) {
                return null;
            }
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

            if (!autoCommit) {
                connection.commit();
                setAutoCommit();
            }
            return new Person(personsId, name, coordinates, dateTime, height, weight, eyesColor, hairsColor, location, ownerId);
        } catch (SQLException e) {
            try {
                if (!autoCommit) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }

    }

    public Person getFirstPerson() {
        try {
            if (autoCommit) {
                setAutoCommit();
            }
            Statement statement = connection.createStatement();
            statement.executeQuery(getFirstPersonQuery);
            ResultSet personFromDb = statement.executeQuery(getFirstPersonQuery);
            if (!personFromDb.next()) {
                return null;
            }
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

            if (!autoCommit) {
                connection.commit();
                setAutoCommit();
            }
            return new Person(personsId, name, coordinates, dateTime, height, weight, eyesColor, hairsColor, location, ownerId);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Person create(PersonDto dto) {
        try {
            if (autoCommit) {
                setAutoCommit();
            }

            PreparedStatement statement = connection.prepareStatement(createPersonQuery);

            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(2, dto.getName());

            Coordinates coordinates = coordinatesDao.create(dto);

            statement.setInt(3, coordinates.getId());
            statement.setLong(4, dto.getHeight());
            statement.setFloat(5, dto.getWeight());
            statement.setString(6, dto.getEyesColor().name());
            statement.setString(7, dto.getHairsColor().name());

            Location location = locationDao.create(dto);

            statement.setInt(8, location.getId());
            statement.setInt(9, dto.getOwnerId());

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String id = resultSet.getString(1);

            Person newPerson = getById(id);
            if (!autoCommit) {
                connection.commit();
                setAutoCommit();
            }
            return newPerson;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!autoCommit) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public Person update(String id, PersonDto dto) {
        try {
            if (autoCommit) {
                setAutoCommit();
            }

            PreparedStatement statement = connection.prepareStatement(updatePersonQuery);

            Person prevPerson = getById(id);
            if (prevPerson == null) {
                throw new IllegalArgumentException("Person with id: " + id + "not found");
            }
            statement.setString(1, dto.getName() == null ? prevPerson.getName() : dto.getName());

            if (dto.getCoordinatesDto() != null) {
                coordinatesDao.update(prevPerson.getCoordinates().getId() + "", dto);
            }

            statement.setLong(2, dto.getHeight() == null ? prevPerson.getHeight() : dto.getHeight());
            statement.setFloat(3, dto.getWeight() == 0 ? prevPerson.getWeight() : dto.getWeight());
            statement.setString(4, dto.getHairsColor() == null ?
                    prevPerson.getHairsColor().name() : dto.getHairsColor().name());

            if (dto.getLocationDto() != null) {
                locationDao.update(prevPerson.getLocation().getId() + "", dto);
            }

            statement.setInt(5, Integer.parseInt(id));
            statement.setInt(6, dto.getOwnerId());
            statement.executeUpdate();
            Person newPerson = getById(id);

            if (!autoCommit) {
                connection.commit();
                setAutoCommit();
            }

            return newPerson;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!autoCommit) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            if (autoCommit) {
                setAutoCommit();
            }
            Person personToDelete = getById(id);
            if (personToDelete == null) {
                throw new IllegalArgumentException("Person with id: " + id + " not found");
            }
            coordinatesDao.deleteById(personToDelete.getCoordinates().getId() + "");
            locationDao.deleteById(personToDelete.getLocation().getId() + "");
            PreparedStatement statement = connection.prepareStatement(deletePersonByIdQuery);
            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
            if (!autoCommit) {
                connection.commit();
                setAutoCommit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!autoCommit) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public List<Person> getAllByOwnerId(String ownerId) {
        List<Person> listOfPerson = new LinkedList<>();
        try {
            if (autoCommit) {
                setAutoCommit();
            }
            PreparedStatement statement = connection.prepareStatement(getAllPersonsByOwnerIdQuery);
            statement.setInt(1, Integer.parseInt(ownerId));
            ResultSet personsFromDb = statement.executeQuery();
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
                int ownerIdentificator = personsFromDb.getInt("ownerId");

                Person person = new Person(id, name, coordinates, dateTime, height, weight, eyesColor, hairsColor, location, ownerIdentificator);
                listOfPerson.add(person);
            }
            if (!autoCommit) {
                connection.commit();
                setAutoCommit();
            }
            return listOfPerson;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!autoCommit) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return listOfPerson;
        }
    }

    public void deleteAllPersonsByOwnerId(String ownerId) {
        try {
            if (autoCommit) {
                setAutoCommit();
            }
            List<Person> listOfPerson = getAllByOwnerId(ownerId);
            if (listOfPerson.isEmpty()) {
                throw new IllegalArgumentException("You have no elements to delete");
            }
            listOfPerson.forEach(person -> {
                coordinatesDao.deleteById(person.getCoordinates().getId() + "");
                locationDao.deleteById(person.getLocation().getId() + "");
            });
            try {
                PreparedStatement statement = connection.prepareStatement(deleteAllPersonsByOwnerIdQuery);
                statement.setInt(1, Integer.parseInt(ownerId));
                statement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if (!autoCommit) {
                connection.commit();
                setAutoCommit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!autoCommit) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }


    @Override
    public void delete(Person element) {

    }

    public boolean checkPersonByOwnerId(String personId, String ownerId) {
        Person person = getById(personId);
        return ownerId.equals(String.valueOf(person.getOwnerId()));
    }

    public int getNumberOfPersons() {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getNumberOfPersonsQuery);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getNumberOfPersonsByOwnerId(String ownerId) {
        try {
            PreparedStatement prepareStatement = connection.prepareStatement(getNumberOfPersonsByOwnerId);
            prepareStatement.setInt(1, Integer.parseInt(ownerId));
            ResultSet resultSet = prepareStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
