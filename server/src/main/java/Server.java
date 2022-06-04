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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class Server {
    private final int port;
    ConnectionModule connectionModule;
    private ExecutorService threadPool;
    private ServerCommandManager[] commandManager;
    LinkedListCollectionManager collectionManager;

    public Server(int port, ServerCommandManager[] commandManager, LinkedListCollectionManager collectionManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.threadPool = Executors.newFixedThreadPool(6);
        this.collectionManager = collectionManager;
    }

    public boolean start() {
        try {
            this.connectionModule = new ConnectionModule(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started on PORT: " + "\u001B[35m" + port + "\u001B[0m");
        return true;
    }

    volatile LinkedList<Request> list = new LinkedList<>();
    volatile LinkedList<Response> resList = new LinkedList<>();

    public void connect() {
//        while (connectionModule.getServerSocket() != null) {
//            try {
////                connectionModule.connect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        Socket client = null;
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


        ExecutorService executor0 = Executors.newFixedThreadPool(6);
        ExecutorService executor1 = Executors.newFixedThreadPool(10);
        ExecutorService executor2 = Executors.newFixedThreadPool(20);
        try {
            while (true) {
//                connectionModule.connect();
                client = connectionModule.getServerSocket().accept();
                System.out.println(client.getLocalSocketAddress());
                ResponseSenderModule out = new ResponseSenderModule(client.getOutputStream());
                RequestHandlerModule in = new RequestHandlerModule(client.getInputStream());

                executor0.execute(new HandlerModule(client, in));
//                System.out.println(executor0 + " " + list.size() + " " + resList.size());
                executor1.execute(new ProcessingRequest(HandlerModule.list, commandManager));
//                System.out.println(executor1 + " " + list.size() + " " + resList.size());
                executor2.execute(new SenderModule(commandManager, out, client, ProcessingRequest.resList));
//                System.out.println(executor2 + " " + list.size() + " " + resList.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//            int i = 1;
//            for (ServerCommandManager commandManagerr : commandManager) {
//                System.out.println(i);
//            while (true) {
//            for (ServerCommandManager commandManagerr : commandManager)

//        threadPool.execute(new CommandWorkerModule(connectionModule.getClientSocket(), commandManager[0], threadPool));

//            }

//                threadPool.submit(new CommandWorkerModule(connectionModule.getClientSocket(), commandManager[1]));
//                threadPool.submit(RequestHandlerModule);
//            }
//            threadPool.shutdown();
//            threadPool.execute(new CommandWorkerModule(connectionModule.getClientSocket(), commandManager[1]));
//            threadPool.execute(new CommandWorkerModule(connectionModule.getClientSocket(), commandManager[2]));

//            }
//    }
    }


}
