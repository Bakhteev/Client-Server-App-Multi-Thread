import commands.*;
import interaction.Request;
import interaction.Response;
import lombok.Getter;
import managers.LinkedListCollectionManager;
import managers.ServerCommandManager;
import utils.Serializator;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.SelectionKey.OP_WRITE;

@Getter
public class Server {
    private final int port;
    ServerSocketChannel serverSocketChannel;
    private ServerCommandManager[] commandManager;
    LinkedListCollectionManager collectionManager;

    public Server(int port, ServerCommandManager[] commandManager, LinkedListCollectionManager collectionManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    public boolean start() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started on PORT: " + "\u001B[35m" + port + "\u001B[0m");
        return true;
    }


    public void connect() {
        ServerCommandManager commandManager = new ServerCommandManager();
        commandManager.addCommands(new AbstractCommand[]{
                new HelpCommand(commandManager),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager.getCollection()),
//                new AddCommand(collectionManager),
                new UpdateCommand(collectionManager),
                new RemoveByIdCommand(collectionManager),
//                new AddIfMinCommand(collectionManager),
                new ExitCommand(),
                new ExecuteScriptCommand(),
                new ClearCommand(collectionManager),
//                new RemoveGreaterCommand(collectionManager),
                new PrintDescendingCommand(collectionManager.getCollection()),
                new PrintUniqueLocationCommand(collectionManager.getCollection()),
                new CountByHeightCommand(collectionManager.getCollection()),
                new RemoveFirstCommand(collectionManager),
        });


        Map<SocketChannel, Response> responseMap = new HashMap<>();

        ExecutorService handler = Executors.newFixedThreadPool(10);
        ExecutorService executor = Executors.newCachedThreadPool();
        ExecutorService sandler = Executors.newFixedThreadPool(10);

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
                            Request request = future.get();
                            Future<Response> responseFuture = executor.submit(() -> commandManager.executeCommand(request));
                            responseMap.put(client, responseFuture.get());
                            client.configureBlocking(false);
                            client.register(key.selector(), OP_WRITE);
                        }
                        if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Response response = responseMap.get(client);
                                    byte[] output = Serializator.serializeObject(response);
                                    ByteBuffer buffer = ByteBuffer.wrap(output);
                                    try {
                                        while (client.write(buffer) > 0) {
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            client.configureBlocking(false);
                            client.register(key.selector(), OP_READ);
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

