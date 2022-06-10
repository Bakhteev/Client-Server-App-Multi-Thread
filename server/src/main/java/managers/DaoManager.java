package managers;

import dao.CoordinatesDao;
import dao.LocationDao;
import dao.PersonDao;
import lombok.Getter;

import java.nio.channels.SocketChannel;
import java.sql.Connection;

@Getter
public class DaoManager {
    public  Connection connection;
    public  CoordinatesDao coordinatesDao;
    public  LocationDao locationDao;
    public  PersonDao personDao;
    public  SocketChannel channel;

    public DaoManager(Connection connection, SocketChannel channel){
        this.channel = channel;
        this.connection = connection;
        coordinatesDao = new CoordinatesDao(connection);
        locationDao=new LocationDao(connection);
        personDao = new PersonDao(connection, locationDao,coordinatesDao);
    }
}
