package commands;

import auth.Auth;
import client.Client;

import interaction.Request;
import workers.ConsoleWorker;

import java.io.IOException;

public class ExitCommand extends AbstractCommand {


    public ExitCommand() {
        super("exit", "exit client side program without saving collection into file", "");

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
        result();
        ConsoleWorker.println("Good bye");
        Client.close();
        System.exit(-1);
        return true;
    }
}
