package managers;

import commands.AbstractCommand;
import exceptions.NoSuchCommandException;
import interaction.Request;
import interaction.Response;

import java.util.LinkedHashMap;

public class ServerCommandManager {
    LinkedHashMap<String, AbstractCommand> commands = new LinkedHashMap<>();

    public void addCommands(AbstractCommand[] commands) {
        for (AbstractCommand command : commands) {
            this.commands.put(command.getName(), command);
        }
    }

    public LinkedHashMap<String, AbstractCommand> getCommands() {
        return commands;
    }

    synchronized public Response executeCommand(Request req, DaoManager daoManager) {
        try {
            if (!commands.containsKey(req.getCommand())) {
                throw new NoSuchCommandException("No such command: " + req.getCommand());
            }
            if(req.getAuthorization() == null){
                return new Response(Response.Status.FAILURE, "Unauthorized");
            }
            if (daoManager.connection == null){
                return new Response(Response.Status.FAILURE, "no connection for u");
            }
            return commands.get(req.getCommand()).execute(req, daoManager);
        } catch (NoSuchCommandException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }
    }
}
