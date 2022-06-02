package dao;

import dto.CoordinatesDto;
import dto.LocationDto;
import dto.PersonDto;
import models.Coordinates;
import models.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LocationDao implements DAO<Location> {

    private final Connection connection;
    private final String getLocationByIdQuery = "SELECT * FROM Locations WHERE id=?";
    private final String createLocationQuery = "INSERT INTO Locations (x, y, z, name) VALUES(?, ?, ?, ?) RETURNING id";

    public LocationDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Location> getAll() {
        return null;
    }

    @Override
    public Location getById(String id) {
        try {
            PreparedStatement statement = connection.prepareStatement(getLocationByIdQuery);
            statement.setInt(1, Integer.parseInt(id));
            ResultSet locationFromDb = statement.executeQuery();
            Location location = null;
            while (locationFromDb.next()) {
                int locationsId = locationFromDb.getInt("id");
                long x = locationFromDb.getLong("x");
                int y = locationFromDb.getInt("y");
                float z = locationFromDb.getFloat("z");
                String name = locationFromDb.getString("name");
                location = new Location(locationsId, x, y, z, name);
                break;
            }
//            statement.executeUpdate();
            return location;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Location();
        }
    }

    @Override
    public Location create(PersonDto dto) {
        LocationDto locationDto = dto.getLocationDto();
        try {
//            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(createLocationQuery);

            statement.setLong(1, locationDto.getX());
            statement.setFloat(2, locationDto.getY());
            statement.setFloat(3, locationDto.getZ());
            statement.setString(4, locationDto.getName());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String id = resultSet.getString(1);

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
    public Location update(String id, PersonDto dto) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void delete(Location element) {

    }
}
