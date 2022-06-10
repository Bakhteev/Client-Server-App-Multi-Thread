import dao.UsersDao;
import dto.UserDto;
import interaction.Request;
import interaction.Response;
import lombok.Getter;
import managers.ConnectionManager;
import managers.ServerCommandManager;
import managers.UserManager;
import models.User;
import modules.RequestHandlerModule;
import modules.ResponseSenderModule;
import org.postgresql.util.PSQLException;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.SelectionKey.OP_WRITE;

@Getter
public class Server {
    private final int port;
    ServerSocketChannel serverSocketChannel;
    private ServerCommandManager commandManager;
    private UserManager userManager = new UserManager();
    private ConnectionManager connectionManager;

    public Server(int port, ServerCommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
    }

    public boolean start() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            connectionManager = new ConnectionManager();
            connectionManager.init();
            Connection connection = connectionManager.getConnection();
            String sqlReqInit =
                    "CREATE TYPE eyesColor AS ENUM ('GREEN', 'BLUE', 'ORANGE');" +
                            "CREATE TYPE hairsColor AS ENUM ('BLACK', 'WHITE', 'BROWN');" +
                            "CREATE TABLE IF NOT EXISTS Coordinates(" +
                            "        id Serial PRIMARY KEY," +
                            "        x integer," +
                            "        y integer" +
                            ");" +
                            "CREATE TABLE IF NOT EXISTS Locations" +
                            "(" +
                            "    id   Serial Unique Primary key," +
                            "    x    integer      NOT NULL," +
                            "    y    integer      NOT NULL," +
                            "    z    float        NOT NULL," +
                            "    name varchar(255) NOT NULL " +
                            ");" +
                            "CREATE TABLE IF NOT EXISTS Users(" +
                            "id SERIAL PRIMARY KEY UNIQUE," +
                            "login VARCHAR(255) NOT NULL UNIQUE," +
                            "password VARCHAR(255) NOT NULL," +
                            "prefix VARCHAR(255) NOT NULL," +
                            "suffix VARCHAR(255) NOT NULL" +
                            ");" +
                            "CREATE TABLE IF NOT EXISTS Persons" +
                            "(" +
                            "    id          SERIAL PRIMARY KEY UNIQUE," +
                            "    date        TIMESTAMP                       NOT NUll," +
                            "    name        VARCHAR(80)                     NOT NULL," +
                            "    coordinates INT REFERENCES Coordinates (id) NOT NULL," +
                            "    height      Int                             NOT NULL," +
                            "    weight      float                           NOT NULL Check (weight > 0)," +
                            "    eyesColor   eyesColor                       NOT NUll," +
                            "    hairsColor  hairsColor                      NOT NUll," +
                            "    location    INT REFERENCES Locations (id)   NOT NUll," +
                            "    ownerId INT REFERENCES Users (id) NOT NULL" +
                            ");";

            try {
                Statement statement = connection.createStatement();
                statement.execute(sqlReqInit);
            } catch (PSQLException ignored) {
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connectionManager.addConnection(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server started on PORT: " + "\u001B[35m" + port + "\u001B[0m");
        return true;
    }

    public void communicate() {
        ExecutorService handler = Executors.newFixedThreadPool(10);
        ExecutorService executor = Executors.newCachedThreadPool();
        LinkedList<SocketChannel> list = new LinkedList<>();
        try {
            Selector selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            SocketChannel sock = serverSocketChannel.accept();
                            if (connectionManager.getSize() == 0) {
                                list.add(sock);
                            } else {
                                userManager.addToConnectionMap(sock, connectionManager.getConnection());
                                userManager.addToDaoManagerMap(sock);
                            }
                            sock.configureBlocking(false);
                            sock.register(key.selector(), OP_READ);
                        }
                        if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            if (list.contains(client)) {
                                if (connectionManager.getSize() != 0) {
                                    userManager.addToConnectionMap(client, connectionManager.getConnection());
                                    userManager.addToDaoManagerMap(client);
                                    list.remove(client);
                                } else {
                                    iterator.remove();
                                    continue;
                                }
                            }
                            Future<Request> future = handler.submit(new RequestHandlerModule(client));
                            try {
                                Request request = future.get();
                                if (request.getCommand().equals("login")) {
                                    login(request, client);
                                } else if (request.getCommand().equals("registration")) {
                                    registration(request, client);
                                } else {
                                    Future<Response> responseFuture = executor.submit(() -> commandManager.executeCommand(request,
                                            UserManager.daoManagerMap.get(client)));
                                    userManager.addToResponseMap(client, responseFuture.get());
                                }
                            } catch (ExecutionException e) {
                                key.cancel();
                                connectionManager.addConnection(userManager.getConnectionMap().get(client));
                                userManager.removeFromDaoManagerMap(client);
                                userManager.removeFromConnectionMap(client);
                                userManager.removeFromResponseMap(client);
                                break;
                            }
                            if (key.isValid()) {
                                if (connectionManager.getSize() != 0) {
                                    userManager.addToConnectionMap(client, connectionManager.getConnection());
                                }
                                client.configureBlocking(false);
                                client.register(key.selector(), OP_WRITE);
                            } else {
                                key.cancel();
                                connectionManager.addConnection(userManager.getConnectionMap().get(client));
                                userManager.removeFromDaoManagerMap(client);
                                userManager.removeFromConnectionMap(client);
                                userManager.removeFromResponseMap(client);
                            }
                        }
                        if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            new Thread(() -> {
                                Response response = userManager.getValueFromResponseMap(client);
                                if (response.getMessage().equals("exit")) {
                                    key.cancel();
                                    connectionManager.addConnection(userManager.getConnectionMap().get(client));
                                    userManager.removeFromDaoManagerMap(client);
                                    userManager.removeFromConnectionMap(client);
                                    userManager.removeFromResponseMap(client);
                                }
                                new ResponseSenderModule(client, response).run();
                            }).start();
                            if (key.isValid()) {
                                if (connectionManager.getSize() != 0) {
                                    userManager.addToConnectionMap(client, connectionManager.getConnection());
                                }
                                client.configureBlocking(false);
                                client.register(key.selector(), OP_READ);
                            } else {
                                connectionManager.addConnection(userManager.getConnectionMap().get(client));
                                userManager.removeFromDaoManagerMap(client);
                                userManager.removeFromConnectionMap(client);
                                userManager.removeFromResponseMap(client);
                            }
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login(Request request, SocketChannel client) {
        try {
            UserDto userDto = (UserDto) request.getBody();
            if (userManager.getConnectionMap().get(client) == null) {
                throw new NullPointerException("No connection");
            }
            UsersDao usersDao = new UsersDao(userManager.getConnectionMap().get(client));
            new Authentication(usersDao).login(userDto);
            User user = usersDao.getByLogin(userDto.getLogin());
            userManager.addToResponseMap(client, new Response(Response.Status.COMPLETED, "", user.getId()));

        } catch (IllegalArgumentException | NullPointerException e) {
            userManager.addToResponseMap(client, new Response(Response.Status.FAILURE, e.getMessage()));

        }
    }

    private void registration(Request request, SocketChannel client) {
        try {
            if (userManager.getConnectionMap().get(client) == null) {
                throw new NullPointerException("No connection");
            }
            new Authentication(new UsersDao(userManager.getConnectionMap().get(client))).
                    registration((UserDto) request.getBody());
            userManager.addToResponseMap(client, new Response<>(Response.Status.COMPLETED, ""));

        } catch (IllegalArgumentException | NullPointerException e) {
            userManager.addToResponseMap(client, new Response<>(Response.Status.FAILURE, ""));
        }
    }
}

