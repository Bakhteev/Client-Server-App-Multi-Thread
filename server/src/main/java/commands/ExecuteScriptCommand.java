package commands;

import interaction.Request;
import interaction.Response;
import managers.DaoManager;


public class ExecuteScriptCommand extends AbstractCommand {


    public ExecuteScriptCommand() {
        super("execute_script", "read and execute script from file setup.", "file_name");

    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {

        return null;
    }

}
