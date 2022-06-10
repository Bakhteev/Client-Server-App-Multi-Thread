package commands;
// TODO: ADD LOGGER

import comparators.PersonDescendingOrderComparator;
import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import models.Person;
import utils.PersonFormatter;

import java.util.LinkedList;

public class PrintDescendingCommand extends AbstractCommand {

    private LinkedList<Person> collection;

    public PrintDescendingCommand(LinkedList<Person> collection) {
        super("print_descending", "display the elements of the collection in descending order.", "");
        this.collection = collection;
    }

    @Override
    public Response<?> execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() != null) {
                throw new IllegalArgumentException("Using of command: " + getName());
            }
            if (daoManager.personDao.getNumberOfPersons() == 0) {
                throw new IllegalArgumentException("Collection is empty");
            }
            StringBuilder sb = new StringBuilder();
            daoManager.personDao.getAll().stream().sorted(new PersonDescendingOrderComparator()).forEachOrdered(person -> {
                sb.append(PersonFormatter.format(person)).append("\n");
            });
            return new Response<>(Response.Status.COMPLETED, "", sb.toString());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }
    }
}
