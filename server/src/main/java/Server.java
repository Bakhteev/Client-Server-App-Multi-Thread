import commands.*;
import interaction.Request;
import interaction.Response;
import lombok.Getter;
import managers.LinkedListCollectionManager;
import managers.ServerCommandManager;
import modules.CommandWorkerModule;
import modules.ConnectionModule;
import modules.RequestHandlerModule;
import modules.ResponseSenderModule;
import test.HandlerModule;
import test.ProcessingRequest;
import test.SenderModule;
import utils.Serializator;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.SelectionKey.OP_WRITE;

@Getter
public class Server {
    private final int port;
    //    ConnectionModule connectionModule;
    ServerSocketChannel serverSocketChannel;
    private ExecutorService threadPool;
    private ServerCommandManager[] commandManager;
    LinkedListCollectionManager collectionManager;

    public Server(int port, ServerCommandManager[] commandManager, LinkedListCollectionManager collectionManager) {
        this.port = port;
        this.commandManager = commandManager;
//        this.threadPool = Executors.newFixedThreadPool(6);
        this.collectionManager = collectionManager;
    }

    public boolean start() {
//        try {
////            this.connectionModule = new ConnectionModule(this.port);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started on PORT: " + "\u001B[35m" + port + "\u001B[0m");
        return true;
    }

//    volatile LinkedList<Request> list = new LinkedList<>();
//    volatile LinkedList<Response> resList = new LinkedList<>();
//    volatile LinkedList<ClientServer> clientArr = new LinkedList<>();

//    @Getter
//    class ClientServer {
//        Socket client;
//        ResponseSenderModule out;
//        RequestHandlerModule in;
//
//        public ClientServer(Socket client, ResponseSenderModule out, RequestHandlerModule in) {
//            this.client = client;
//            this.out = out;
//            this.in = in;
//        }
//    }

    public void connect() {
//        while (connectionModule.getServerSocket() != null) {
//            try {
////                connectionModule.connect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

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

//
//        ExecutorService executor0 = Executors.newFixedThreadPool(6);
//        ExecutorService executor1 = Executors.newFixedThreadPool(10);
//        ExecutorService executor2 = Executors.newFixedThreadPool(20);

        Map<SocketChannel, Response> responseMap = new HashMap<>();

        ExecutorService handler = Executors.newFixedThreadPool(10);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        ExecutorService sandler = Executors.newFixedThreadPool(10);

        ByteBuffer buffer = ByteBuffer.allocate(10000);
        try {
//            ServerSocketChannel server = ServerSocketChannel.open();
//            server.bind(new InetSocketAddress(5000));
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
                            System.out.println("accept");
                            sock.configureBlocking(false);
                            sock.register(key.selector(), OP_READ);
                        }
                        if (key.isReadable()) {
                            System.out.println("read");
                            SocketChannel client = (SocketChannel) key.channel();
                            System.out.println(client);
                            Future<Request> future = handler.submit(() -> {
                                int size = client.read(buffer);
//                                while (client.read(buffer) == 0) {
//                                }
                                return Serializator.deserializeObject(buffer.array());
                            });
                            Request request = future.get();
                            System.out.println(request);
                            Future<Response> responseFuture = executor.submit(() -> commandManager.executeCommand(request));
                            responseMap.put(client, responseFuture.get());
                            client.configureBlocking(false);
                            client.register(key.selector(), OP_WRITE);
                        }
                        if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            sandler.execute(() -> {
                                Response response = responseMap.get(client);
                                System.out.println(response);
                                byte[] output = Serializator.serializeObject(response);
                                buffer.clear();
                                buffer.put(output);
//                                System.out.println(Arrays.toString(buffer.array()));
                                buffer.flip();
//                                System.out.println(Arrays.toString(buffer.array()));

                                try {
//                                    while (client.write(buffer) > 0) {
                                        client.write(buffer);
                                        System.out.println(Arrays.toString(buffer.array()));
//                                        client.write(buffer);
//                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            client.configureBlocking(false);
                            client.register(key.selector(), OP_READ);
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

