import commands.*;
import dao.UsersDao;
import dto.UserDto;
import interaction.Request;
import interaction.Response;
import lombok.Getter;
import managers.ConnectionManager;
import managers.LinkedListCollectionManager;
import managers.ServerCommandManager;
import managers.UserManager;
import models.User;
import utils.Serializator;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
    LinkedListCollectionManager collectionManager;
    private UserManager userManager = new UserManager();
    private ConnectionManager connectionManager;

    public Server(int port, ServerCommandManager commandManager, LinkedListCollectionManager collectionManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    public boolean start() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            connectionManager = new ConnectionManager();
            connectionManager.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started on PORT: " + "\u001B[35m" + port + "\u001B[0m");
        return true;
    }

    public void communicate() {
        ExecutorService handler = Executors.newFixedThreadPool(10);
        ExecutorService executor = Executors.newCachedThreadPool();

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
                            userManager.addToConnectionMap(sock, connectionManager.getConnection());
                            userManager.addToDaoManagerMap(sock);
                            sock.configureBlocking(false);
                            sock.register(key.selector(), OP_READ);
                        }
                        if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            System.out.println(client);
                            Future<Request> future = handler.submit(() -> {
                                ByteBuffer buffer = ByteBuffer.allocate(5000);
                                client.read(buffer);
                                return Serializator.deserializeObject(buffer.array());
                            });
                            try {
                                Request request = future.get();

                                if (request.getCommand().equals("login")) {
                                    try {
                                        UserDto userDto = (UserDto) request.getBody();
                                        UsersDao usersDao = new UsersDao(userManager.getConnectionMap().get(client));
                                        new Authentication(usersDao).login(userDto);
                                        User user = usersDao.getByLogin(userDto.getLogin());
                                        userManager.addToResponseMap(client, new Response(Response.Status.COMPLETED, "", user.getId()));

                                    } catch (IllegalArgumentException e) {
                                        userManager.addToResponseMap(client, new Response(Response.Status.FAILURE, e.getMessage()));
                                    }

                                } else if (request.getCommand().equals("registration")) {
                                    try {
                                        new Authentication(new UsersDao(userManager.getConnectionMap().get(client))).
                                                registration((UserDto) request.getBody());
                                        userManager.addToResponseMap(client, new Response(Response.Status.COMPLETED, ""));

                                    } catch (IllegalArgumentException e) {
                                        userManager.addToResponseMap(client, new Response(Response.Status.FAILURE, ""));

                                    }
                                } else {
                                    Future<Response> responseFuture = executor.submit(() -> commandManager.executeCommand(request,
                                            UserManager.daoManagerMap.get(client)));
                                    userManager.addToResponseMap(client, responseFuture.get());
                                }
                            } catch (ExecutionException e) {
                                key.cancel();
                                break;
                            }
                            if (key.isValid()) {
                                client.configureBlocking(false);
                                client.register(key.selector(), OP_WRITE);
                            } else {
                                connectionManager.addConnection(userManager.getConnectionMap().get(client));
                                userManager.removeFromDaoManagerMap(client);
                                userManager.removeFromConnectionMap(client);
                                userManager.removeFromResponseMap(client);
                                key.cancel();
                            }
                        }
                        if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            new Thread(() -> {
                                Response response = userManager.getValueFromResponseMap(client);
                                if (response.getMessage().equals("exit")) {
                                    connectionManager.addConnection(userManager.getConnectionMap().get(client));
                                    userManager.removeFromDaoManagerMap(client);
                                    userManager.removeFromConnectionMap(client);
                                    userManager.removeFromResponseMap(client);
                                    key.cancel();
                                }
                                byte[] output = Serializator.serializeObject(response);
                                ByteBuffer buffer = ByteBuffer.wrap(output);
                                try {
                                    while (client.write(buffer) > 0) {
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            if (key.isValid()) {
                                client.configureBlocking(false);
                                client.register(key.selector(), OP_READ);
                            } else {
                                connectionManager.addConnection(userManager.getConnectionMap().get(client));
                                userManager.removeFromDaoManagerMap(client);
                                userManager.removeFromConnectionMap(client);
                                userManager.removeFromResponseMap(client);
                                key.cancel();
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
}

