package test;

import interaction.Request;
import modules.RequestHandlerModule;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class HandlerModule implements Runnable {
    Socket client;
    public static LinkedList<Request> list;
    RequestHandlerModule in;

    public HandlerModule(Socket client, RequestHandlerModule in) {
        this.client = client;
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request req = in.readRequest();
                System.out.println(req + " : " + Thread.currentThread().getName());
                list.add(req);
                System.out.println(list);
                break;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
