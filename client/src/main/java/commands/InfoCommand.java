package commands;

import client.Client;
import communicate.RequestSender;
import communicate.ResponseHandler;
import interaction.Request;
import interaction.Response;
import workers.ConsoleWorker;

import java.io.IOException;

public class InfoCommand extends AbstractCommand {
    RequestSender writer;
    ResponseHandler reader;

    public InfoCommand(RequestSender writer, ResponseHandler reader) {
        super("info", "print information about the collection to standard output.", "");
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
        Client.sendRequest(new Request<>(getName()));

        return result(reader);
    }
}
