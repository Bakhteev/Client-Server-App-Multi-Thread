package test;

import interaction.Request;
import interaction.Response;
import managers.ServerCommandManager;
import modules.ResponseSenderModule;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class SenderModule implements Runnable {

    ServerCommandManager commandManager;
    ResponseSenderModule out;
    Socket client;
    LinkedList<Response> list;

    public SenderModule(ServerCommandManager commandManager, ResponseSenderModule out, Socket client, LinkedList<Response> list) {
        this.commandManager = commandManager;
        this.out = out;
        this.client = client;
        this.list = list;
    }

    @Override
    public void run() {
//        System.out.println(list);
        while (client.isConnected()) {
            ListIterator<Response> iterator = list.listIterator();
            while (iterator.hasNext()) {
                try {
                    out.sendResponse(iterator.next());
                    iterator.remove();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

//            for(Request req : list){
//                Response res = commandManager.executeCommand(req);
//                try {
//                    out.sendResponse(res);
//                } catch (IOException e) {
//                    e.printStackTrace();
        }
    }
}

