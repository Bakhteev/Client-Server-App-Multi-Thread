package commands;

import interaction.Request;
import interaction.Response;
import managers.DaoManager;

public class ServerExitCommand extends AbstractCommand {

    public ServerExitCommand() {
        super("server_exit", "closes the server and saves progress", "");
    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {
        try {
            System.out.println("\u001B[32mServer is closing\u001B[0m");
            System.exit(0);
            return null;
        } catch (SecurityException exception) {
            System.out.println("\u001B[31mFile permission error!\u001B[0m");
            return null;
        }
    }
}
