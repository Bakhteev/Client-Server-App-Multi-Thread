package modules;

import interaction.Request;
import interaction.Response;
import managers.ServerCommandManager;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CommandWorkerModule implements Runnable {
    private static RequestHandlerModule reader;
    private static ResponseSenderModule writer;
    private static Socket clientSocket;
    private ServerCommandManager commandManager;
    private ExecutorService threadPool;

    public CommandWorkerModule(Socket clientSocket, ServerCommandManager commandManager, ExecutorService threadPool) {
        CommandWorkerModule.clientSocket = clientSocket;
        this.commandManager = commandManager;
        this.threadPool = threadPool;
    }

    public void setup() {
        try {
            writer = new ResponseSenderModule(clientSocket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println(e.getMessage());
        }
        try {
            reader = new RequestHandlerModule(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println(e.getMessage());
        }
    }

    public static void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleRequest() {


        Request request = null;
        List<Request> arrList = new ArrayList<>();
        try {
            Future<Request> FutureReq = threadPool.submit(reader);
            System.out.println(FutureReq.isDone());
            while (!FutureReq.isDone()){
                request = FutureReq.get();
                arrList.add(request);
//                System.out.println(Arrays.toString(FutureReq.get()));
            }
            System.out.println(arrList);
//                    FutureReq.get();
//            request = reader.readRequest();
//            request = FutureReq.get();
            System.out.println(request);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (request == null) {
            close();
            return;
        }
        try {
            if (request.getCommand().equals("exit")) {
                close();
                System.out.println("\u001B[34mСlient closed\u001B[0m");
                return;
            }

            Response<?> res = commandManager.executeCommand(request);
            writer.setRes(res);
            threadPool.execute(writer);
//            writer.sendResponse(res);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //    @Override
    public synchronized void run() {
        setup();
        while (!clientSocket.isClosed()) {
            handleRequest();
        }
        close();
    }
}
