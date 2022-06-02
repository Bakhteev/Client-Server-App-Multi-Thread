package dao;

import dto.CoordinatesDto;
import dto.PersonDto;
import models.Coordinates;
import models.Location;

import java.sql.*;
import java.util.List;

public class CoordinatesDao implements DAO<Coordinates> {

    private final Connection connection;
    private final String getCoordinatesByIdQuery = "SELECT * FROM Coordinates WHERE id=?;";
    private final String createCoordinatesQuery = "INSERT INTO Coordinates (x,y) VALUES(?,?) RETURNING id;";

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
            coordinatesFromDb.next();
            int coordinatesId = coordinatesFromDb.getInt("id");
            int x = coordinatesFromDb.getInt("x");
            int y = coordinatesFromDb.getInt("y");
            return new Coordinates(coordinatesId, x, y);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Coordinates();
        }
    }

    @Override
    public Coordinates create(PersonDto dto) {
        CoordinatesDto coordinatesDto = dto.getCoordinatesDto();
        try {
//            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(createCoordinatesQuery);
            statement.setInt(1, coordinatesDto.getX());
            statement.setFloat(2, coordinatesDto.getY());
            ResultSet resultSet = statement.executeQuery();
//            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            String id = statement.getResultSet().getString(1);

//            connection.commit();
//            connection.setAutoCommit(true);
            return getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
//            try {
//                connection.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
            return null;
        }
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
