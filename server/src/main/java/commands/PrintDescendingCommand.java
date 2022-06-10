package commands;

import comparators.PersonDescendingOrderComparator;
import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import utils.PersonFormatter;


public class PrintDescendingCommand extends AbstractCommand {


    public PrintDescendingCommand() {
        super("print_descending", "display the elements of the collection in descending order.", "");
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
