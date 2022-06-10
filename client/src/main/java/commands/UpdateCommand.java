package commands;

import auth.Auth;
import client.Client;

import dto.PersonDto;
import interaction.Request;
import maker.PersonMaker;
import managers.ClientCommandManager;
import workers.ConsoleWorker;

import java.io.IOException;

public class UpdateCommand extends AbstractCommand {
    ClientCommandManager commandManager;

    public UpdateCommand(ClientCommandManager commandManager) {
        super("update", "update the value of the collection element whose id is equal to the given one.",
                "id {element}");
        this.commandManager = commandManager;
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
        PersonMaker maker;
        if (ClientCommandManager.fileMode) {
            maker = new PersonMaker(commandManager.getScanners().getLast());
        } else {
            maker = new PersonMaker(ClientCommandManager.console);
        }
        PersonDto dto = maker.update();
        try {
            Client.sendRequest(new Request<>(getName(), argument, dto, Auth.authHeader));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result();
    }
}
