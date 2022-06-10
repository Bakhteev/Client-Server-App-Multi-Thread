package commands;

import auth.Auth;
import client.Client;

import interaction.Request;
import workers.ConsoleWorker;

import java.io.IOException;

public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "display help on available commands.", "");

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
        return result();
    }
}
