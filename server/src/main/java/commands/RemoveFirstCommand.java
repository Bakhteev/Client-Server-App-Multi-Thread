package commands;

import dao.PersonDao;
import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import models.Person;

public class RemoveFirstCommand extends AbstractCommand {

    public RemoveFirstCommand() {
        super("remove_first", "remove first element of collection", "");
    }

    @Override
    public Response<?> execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() != null) {
                throw new IllegalArgumentException("Using of command: " + getName());
            }
            PersonDao personDao = daoManager.getPersonDao();
            if (personDao.getNumberOfPersons() == 0) {
                throw new IllegalArgumentException("Collection is empty");
            }
            Person personToDelete = personDao.getFirstPerson();
            if (personToDelete.getOwnerId() == req.getAuthorization()) {
                personDao.deleteById(personToDelete.getId() + "");
                return new Response<>(Response.Status.COMPLETED, "", "First element was successfully deleted");
            } else {
                return new Response<>(Response.Status.FAILURE, "You have no permission to delete this element");
            }
        } catch (IllegalArgumentException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }
    }
}
