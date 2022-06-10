package commands;

import client.Client;

import interaction.Response;
import workers.ConsoleWorker;


public abstract class AbstractCommand {
    private String name;
    private String description;
    private String params;

    public AbstractCommand(String name, String description, String params) {
        this.name = name;
        this.description = description;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getParameters() {
        return params;
    }

    public boolean result() {
        Response res = Client.getResponse();
        if (res.getStatus() == Response.Status.FAILURE) {
            ConsoleWorker.printError(res.getMessage());
            return false;
        }
        ConsoleWorker.println((String) res.getBody());
        return true;

    }

    abstract public boolean execute(String argument);


}
