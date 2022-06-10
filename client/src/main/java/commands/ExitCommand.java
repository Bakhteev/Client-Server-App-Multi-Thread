package commands;

import auth.Auth;
import client.Client;
import communicate.RequestSender;
import communicate.ResponseHandler;
import interaction.Request;
import workers.ConsoleWorker;

import java.io.IOException;

public class ExitCommand extends AbstractCommand {
    RequestSender writer;
    ResponseHandler reader;

    public ExitCommand(RequestSender writer, ResponseHandler reader) {
        super("exit", "exit client side program without saving collection into file", "");
        this.writer = writer;
        this.reader = reader;
    }

    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) {
                throw new IllegalArgumentException("Using of command: " + getName());
            }
        } catch (IllegalArgumentException e) {
            ConsoleWorker.printError(e.getMessage());
            return false;
        }
        try {
            Client.sendRequest(new Request<>(getName(), Auth.authHeader));
        } catch (IOException e) {
            e.printStackTrace();
        }
        result(reader);
        ConsoleWorker.println("Good bye");
        Client.close();
        System.exit(-1);
        return true;
    }
}
