package commands;

import dao.PersonDao;
import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import utils.PersonFormatter;



public class ShowCommand extends AbstractCommand {

    public ShowCommand() {
        super("show", "print to standard output all elements of the collection in string representation.", "");
    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() != null) {
                throw new IllegalArgumentException("Using of command: " + getName());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }
        PersonDao personDao = daoManager.personDao;
        if (personDao.getNumberOfPersons() == 0) {
            return new Response(Response.Status.COMPLETED, "", "Collection is empty");
        }
        return new Response<>(Response.Status.COMPLETED, "", PersonFormatter.formatCollection(personDao.getAll()));
    }
}
