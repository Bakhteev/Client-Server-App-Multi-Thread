package managers;

import interaction.Response;
import lombok.Getter;

import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Getter
public class UserManager {
    Map<SocketChannel, Response> responseMap = new HashMap<>();
    Map<SocketChannel, Connection> connectionMap = new HashMap<>();
    public static Map<SocketChannel, DaoManager> daoManagerMap = new HashMap<>();


    public void addToResponseMap(SocketChannel socketChannel, Response res) {
        responseMap.put(socketChannel, res);
    }

    public void addToConnectionMap(SocketChannel socketChannel, Connection connection) {
        connectionMap.put(socketChannel, connection);
    }

    public void removeFromResponseMap(SocketChannel socketChannel) {
        responseMap.remove(socketChannel);
    }

    public void removeFromConnectionMap(SocketChannel socketChannel) {
        responseMap.remove(socketChannel);
    }

    public Response getValueFromResponseMap(SocketChannel socketChannel) {
        return responseMap.get(socketChannel);
    }

    public void addToDaoManagerMap(SocketChannel socketChannel) {
        daoManagerMap.put(socketChannel, new DaoManager(connectionMap.get(socketChannel)));
    }

    public void removeFromDaoManagerMap(SocketChannel socketChannel) {
        daoManagerMap.remove(socketChannel);
    }

}
