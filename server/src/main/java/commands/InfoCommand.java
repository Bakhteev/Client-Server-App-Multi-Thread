package commands;


import interaction.Request;
import interaction.Response;
import managers.DaoManager;

public class InfoCommand extends AbstractCommand {

    public InfoCommand() {
        super("info", "print information about the collection to standard output.", "");
    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() != null)
                throw new IllegalArgumentException("Using of command: " + getName());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return new Response(Response.Status.FAILURE, e.getMessage());
        }

        String body =
                "Number of elements: " + daoManager.personDao.getNumberOfPersons() + "\n"
                        + "Number of your    elements: " + daoManager.personDao.getNumberOfPersonsByOwnerId(req.getAuthorization()+"");
        return new Response<>(Response.Status.COMPLETED, "", body);
    }
}
