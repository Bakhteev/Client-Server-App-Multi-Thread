package commands;

import dto.PersonDto;
import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import managers.LinkedListCollectionManager;
import models.Person;
import utils.PersonFormatter;


public class AddCommand extends AbstractCommand {
    LinkedListCollectionManager collectionManager;

    public AddCommand(LinkedListCollectionManager collectionManager) {
        super("add", "add a new element to the collection.", "{element}");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {
        PersonDto dto = (PersonDto) req.getBody();
        dto.setOwnerId(req.getAuthorization());
        daoManager.personDao.create(dto);
        return new Response<>(Response.Status.COMPLETED, "", "Person has successfully added");
    }
}
