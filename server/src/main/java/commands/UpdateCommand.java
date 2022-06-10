package commands;

import dao.PersonDao;
import dto.PersonDto;
import exceptions.PersonNotFoundException;
import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import models.Person;
import utils.PersonFormatter;

public class UpdateCommand extends AbstractCommand {

    public UpdateCommand() {
        super("update", "update the value of the collection element whose id is equal to the given one.",
                "id {element}");
    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() == null) {
                throw new IllegalArgumentException("Using of command :" + getName() + " " + getParameters());
            }
        } catch (IllegalArgumentException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }
        try {
            PersonDao personDao = daoManager.getPersonDao();
            Person personToUpdate = personDao.getById(req.getParams());
            if (personToUpdate == null) {
                throw new PersonNotFoundException("Person with id: " + req.getParams() + "wasn't found");
            }
            if (personToUpdate.getOwnerId() != req.getAuthorization()) {
                return new Response<>(Response.Status.FAILURE, "You don have permission to update this element");
            }
            PersonDto dto = (PersonDto) req.getBody();
            dto.setOwnerId(personToUpdate.getOwnerId());
            personDao.update(req.getParams(), dto);
            return new Response<>(Response.Status.COMPLETED, "", PersonFormatter.format(personDao.getById(req.getParams())));
        } catch (NumberFormatException e) {
            return new Response<>(Response.Status.FAILURE, "Wrong id Format: " + req.getParams());
        } catch (PersonNotFoundException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }
    }
}
