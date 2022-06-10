package commands;
// TODO ADD LOGGER

import dao.PersonDao;
import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import models.Person;

import java.util.LinkedList;

public class CountByHeightCommand extends AbstractCommand {

    public CountByHeightCommand() {
        super("count_by_height", "display the number of elements whose height field value is equal to the given one.",
                "height");
    }


    @Override
    public Response<?> execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() == null) {
                throw new IllegalArgumentException("Using of command: " + getName() + " " + getParameters());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }

        PersonDao personDao = daoManager.personDao;

        if (personDao.getNumberOfPersons() == 0) {
            return new Response<>(Response.Status.FAILURE, "Table is empty");
        }
        try {
            long height = Long.parseLong(req.getParams());
            long result = personDao.getAll().stream().filter( p -> p.getHeight() == height).count();
//            System.out.println("number of elements: " + result);
            return new Response<>(Response.Status.COMPLETED, "", "number of elements: " + result);
        } catch (NumberFormatException e) {
//            System.out.println(req.getParams() + " is not a number");
            return new Response<>(Response.Status.FAILURE, req.getParams() + " is not a number");
        }
    }
}
