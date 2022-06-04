package test;

import interaction.Request;
import interaction.Response;
import managers.ServerCommandManager;

import java.util.LinkedList;
import java.util.ListIterator;

public class ProcessingRequest implements Runnable {

    LinkedList<Request> reqList;
    public static LinkedList<Response> resList = new LinkedList<>();
    ServerCommandManager commandManager;

    public ProcessingRequest(LinkedList<Request> reqList, ServerCommandManager commandManager) {
        this.reqList = reqList;
        this.commandManager = commandManager;
    }

    @Override
    public void run() {
//        System.out.println(reqList.size() + " Procces" );
        ListIterator<Request> iterator = reqList.listIterator();
        while (iterator.hasNext()) {
            Request req = iterator.next();
            resList.add(commandManager.executeCommand(req));
            iterator.remove();
        }
    }
}
