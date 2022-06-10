package commands;


import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import models.Person;

public class RemoveByIdCommand extends AbstractCommand {

    public RemoveByIdCommand() {
        super("remove_by_id", "remove element from collection by its id.", "id");
    }

    @Override
    public Response<?> execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() == null) {
                throw new IllegalArgumentException("Using of command :" + getName() + " " + getParameters());
            }
        } catch (IllegalArgumentException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }
        try {
            Person person = daoManager.personDao.getById(req.getParams());
            if (person == null) {
                return new Response<>(Response.Status.FAILURE, "Person with id: " + req.getParams() + " not found");
            }
            if (person.getOwnerId() != req.getAuthorization()) {
                return new Response<>(Response.Status.FAILURE, "You have no permission to delete this element");
            }
            daoManager.personDao.deleteById(req.getParams());
            return new Response<>(Response.Status.COMPLETED, "", "Element was successfully deleted");
        } catch (NumberFormatException e) {
            System.out.println("Wrong id format");
            return new Response<>(Response.Status.FAILURE, "Wrong id format");
        }
    }
}
