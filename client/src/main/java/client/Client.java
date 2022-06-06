package client;

import commands.*;
import communicate.RequestSender;
import communicate.ResponseHandler;
import interaction.Request;
import interaction.Response;
import managers.ClientCommandManager;
import utils.Serializator;
import workers.ConsoleWorker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class Client {
    private static int port;
    private static String host;
    private static SocketChannel socket;
    private static RequestSender writer;
    private static ResponseHandler reader;
    private static ByteBuffer buffer = ByteBuffer.allocate(10000);


    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public SocketChannel getSocket() {
        return socket;
    }

    public boolean connect() {
        try {
            socket = SocketChannel.open(new InetSocketAddress(host, port));
        } catch (IOException e) {
            ConsoleWorker.printError(e.getMessage());
            return false;
        }
        return true;
    }


    public static void setup() {
//        try {
//            buffer = ByteBuffer.allocate(10000);
//            writer = new RequestSender(socket.socket().getOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
////            reader = new ResponseHandler(socket.socket().getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void close() {
//        try {
//            writer.close();
//        } catch (IOException e) {
//        }
//        try {
//            reader.close();
//        } catch (IOException e) {
//        }
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    public static void waitConnection() {
        int sec = 0;
        close();
        while (!socket.socket().isConnected()) {
            try {
                socket = SocketChannel.open(new InetSocketAddress(InetAddress.getByName(host), port));
//                reader.setReader(socket.socket().getInputStream());
//                writer.setWriter(socket.socket().getOutputStream());
                ConsoleWorker.println("\u001B[32mReconnection completed successfully. Continuation of execution.\u001B[0m");
                return;
            } catch (IOException e) {
            }
            ConsoleWorker.println("\rConnection error. Waiting for reconnect: " + sec + "/60 seconds");
            sec++;
            if (sec > 60) {
                System.exit(0);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void run() {
        while (true) {
            get();
        }
    }


    public void get() {
        ClientCommandManager commandManager = new ClientCommandManager();
        commandManager.addCommands(new AbstractCommand[]{
                new HelpCommand(writer, reader),
                new InfoCommand(writer, reader),
                new CountByHeightCommand(writer, reader),
                new PrintDescendingCommand(writer, reader),
//                new AddCommand(writer, reader, commandManager),
//                new UpdateCommand(writer, reader, commandManager),
                new ShowCommand(writer, reader),
                new ClearCommand(writer, reader),
//                new RemoveGreaterCommand(writer, reader, commandManager),
                new RemoveFirstCommand(writer, reader),
                new RemoveByIdCommand(writer, reader),
                new PrintUniqueLocationCommand(writer, reader),
                new ExecuteScriptCommand(commandManager),
                new ExitCommand(writer, reader),
//                new AddIfMinCommand(writer, reader, commandManager)
        });

        commandManager.startInteractiveMode();

    }

    public static void sendRequest(Request req) {
        byte[] bytes = Serializator.serializeObject(req);
        try {
            buffer = ByteBuffer.wrap(bytes);
            socket.write(buffer);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Response getResponse() {
        try {
            buffer.clear();
//            System.out.println(Arrays.toString(buffer.array()));
            buffer.flip();
            while (socket.read(buffer) > 0) {
//                socket.read(buffer);
//                socket.read(buffer);
            }
            System.out.println(Arrays.toString(buffer.array()));
//            buffer.flip();
            Response response = Serializator.deserializeObject(buffer.array());
            System.out.println(response);
            buffer.clear();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

