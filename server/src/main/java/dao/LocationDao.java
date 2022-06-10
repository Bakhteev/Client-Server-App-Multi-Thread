package dao;

import dto.CoordinatesDto;
import dto.LocationDto;
import dto.PersonDto;
import models.Coordinates;
import models.Location;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class LocationDao implements DAO<Location> {

    private final Connection connection;
    private final String getAllLocationsQuery = "SELECT * FROM Locations";
    private final String getLocationByIdQuery = "SELECT * FROM Locations WHERE id=?";
    private final String createLocationQuery = "INSERT INTO Locations (x, y, z, name) VALUES(?, ?, ?, ?) RETURNING id";
    private final String deleteLocationQuery = "DELETE FROM Locations WHERE id=?";

    public LocationDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Location> getAll() {
        List<Location> listOfLocation = new LinkedList<>();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery(getAllLocationsQuery);

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int locationsId = resultSet.getInt("id");
                long x = resultSet.getLong("x");
                int y = resultSet.getInt("y");
                float z = resultSet.getFloat("z");
                String name = resultSet.getString("name");
                listOfLocation.add(new Location(locationsId, x, y, z, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfLocation;
    }

    @Override
    public Location getById(String id) {
        try {
            PreparedStatement statement = connection.prepareStatement(getLocationByIdQuery);
            statement.setInt(1, Integer.parseInt(id));
            ResultSet locationFromDb = statement.executeQuery();
            locationFromDb.next();
            int locationsId = locationFromDb.getInt("id");
            long x = locationFromDb.getLong("x");
            int y = locationFromDb.getInt("y");
            float z = locationFromDb.getFloat("z");
            String name = locationFromDb.getString("name");
            return new Location(locationsId, x, y, z, name);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Location();
        }
    }

    @Override
    public Location create(PersonDto dto) {
        LocationDto locationDto = dto.getLocationDto();
        try {
            PreparedStatement statement = connection.prepareStatement(createLocationQuery);

            statement.setLong(1, locationDto.getX());
            statement.setFloat(2, locationDto.getY());
            statement.setFloat(3, locationDto.getZ());
            statement.setString(4, locationDto.getName());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String id = resultSet.getString(1);
            return getById(id);
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public Location update(String id, PersonDto dto) {
        return null;
    }

    @Override
    public void deleteById(String id) {
        try {
            PreparedStatement statement = connection.prepareStatement(deleteLocationQuery);
            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Location element) {

    }
}
