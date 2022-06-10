package commands;

import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import managers.ServerCommandManager;


public class HelpCommand extends AbstractCommand {
    ServerCommandManager commandManager;

    public HelpCommand(ServerCommandManager commandManager) {
        super("help", "display help on available commands.", "");
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() != null) {
                throw new IllegalArgumentException("Using of command: " + getName());
            }
            StringBuilder sb = new StringBuilder();
            commandManager.getCommands().forEach((key, value) -> sb.append(key).append(": ").append(value.getDescription()).append(" ").append(
                    "Params: ").append(value.getParameters().isEmpty() ? "none" : value.getParameters()).append("\n"));
            return new Response<>(Response.Status.COMPLETED, "", String.valueOf(sb));
        } catch (IllegalArgumentException exception) {
            System.out.println("Использование: '" + getName() + "'");
            return new Response<>(Response.Status.FAILURE, "Использование: '" + getName() + "'");
        }
    }


}
