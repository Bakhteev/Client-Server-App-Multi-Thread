package commands;

import auth.Auth;
import client.Client;

import interaction.Request;
import workers.ConsoleWorker;

import java.io.IOException;

public class RemoveByIdCommand extends AbstractCommand {


    public RemoveByIdCommand() {
        super("remove_by_id", "remove element from collection by its id.", "id");

    }


    @Override
    public boolean execute(String argument) {
        try {
            if (argument.isEmpty()) {
                throw new IllegalArgumentException("Using of command :" + getName() + " " + getParameters());
            }
        } catch (IllegalArgumentException e) {
            ConsoleWorker.printError(e.getMessage());
            return false;
        }
        try {
            Client.sendRequest(new Request<>(getName(), argument, Auth.authHeader));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result();
    }
}
