package commands;

import auth.Auth;
import client.Client;
import communicate.RequestSender;
import communicate.ResponseHandler;
import interaction.Request;
import interaction.Response;
import workers.ConsoleWorker;

import java.io.IOException;

public class CountByHeightCommand extends AbstractCommand {

    RequestSender writer;
    ResponseHandler reader;

    public CountByHeightCommand(RequestSender writer, ResponseHandler reader) {
        super("count_by_height", "display the number of elements whose height field value is equal to the given one.",
                "height");
        this.writer = writer;
        this.reader = reader;
    }


    @Override
    public boolean execute(String argument) {
        try {
            if (argument.isEmpty()) {
                throw new IllegalArgumentException("Using of command: " + getName() + " " + getParameters());
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

        return result(reader);
    }
}
