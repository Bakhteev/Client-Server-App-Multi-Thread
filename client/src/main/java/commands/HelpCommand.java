package commands;

import communicate.RequestSender;
import communicate.ResponseHandler;
import interaction.Request;
import interaction.Response;
import workers.ConsoleWorker;

import java.io.IOException;

public class HelpCommand extends AbstractCommand {

//    ClientCommandManager commandManager;

    RequestSender writer;
    ResponseHandler reader;

    public HelpCommand(RequestSender writer, ResponseHandler reader) {
        super("help", "display help on available commands.", "");
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
            writer.sendRequest(new Request<>(getName()));
        } catch (IOException e) {

            e.printStackTrace();
        }
        return result(reader);
    }
}
