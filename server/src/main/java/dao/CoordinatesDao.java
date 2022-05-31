package dao;

import dto.PersonDto;
import models.Coordinates;
import models.Location;

import java.sql.*;
import java.util.List;

public class CoordinatesDao implements DAO<Coordinates> {

    private final Connection connection;
    private final String getCoordinatesByIdQuery = "SELECT * FROM Coordinates WHERE id=?";

    public CoordinatesDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Coordinates> getAll() {
        return null;
    }

    @Override
    public Coordinates getById(String id) {
        try {
            PreparedStatement statement = connection.prepareStatement(getCoordinatesByIdQuery);
            statement.setInt(1, Integer.parseInt(id));
            ResultSet coordinatesFromDb = statement.executeQuery();
            Coordinates coordinates = null;
            while (coordinatesFromDb.next()) {
                int coordinatesId = coordinatesFromDb.getInt("id");
                int x = coordinatesFromDb.getInt("x");
                int y = coordinatesFromDb.getInt("y");
                coordinates = new Coordinates(coordinatesId, x, y);
                break;
            }
//            statement.executeUpdate();
            return coordinates;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Coordinates();
        }
    }

    @Override
    public Coordinates create(PersonDto dto) {
        return null;
    }

    @Override
    public Coordinates update(String id, PersonDto dto) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void delete(Coordinates element) {

    }
}
