package commands;

import interaction.Request;
import interaction.Response;
import managers.DaoManager;

public class ExitCommand extends AbstractCommand {

    public ExitCommand() {
        super("exit", "exit program without saving collection into file", "");
    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {
        return new Response<>(Response.Status.FAILURE, "exit");
    }
}
