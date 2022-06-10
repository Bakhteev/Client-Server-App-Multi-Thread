package commands;

import auth.Auth;
import client.Client;
import dto.PersonDto;
import interaction.Request;
import maker.PersonMaker;
import managers.ClientCommandManager;
import workers.ConsoleWorker;

import java.io.IOException;

public class AddCommand extends AbstractCommand {

    ClientCommandManager commandManager;

    public AddCommand(ClientCommandManager commandManager) {
        super("add", "add a new element to the collection.", "{element}");
        this.commandManager = commandManager;
    }

    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty())
                throw new IllegalArgumentException("Using of command add: " + getName());

        } catch (IllegalArgumentException e) {
            ConsoleWorker.printError(e.getMessage());
            return false;
        }
        PersonMaker maker;
        if (ClientCommandManager.fileMode) {
            maker = new PersonMaker(commandManager.getScanners().getLast());
        } else {
            maker = new PersonMaker(ClientCommandManager.console);
        }
        PersonDto dto = maker.makeDto();
        try {
            Client.sendRequest(new Request<>(getName(), null,dto, Auth.authHeader));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result();
    }
}
