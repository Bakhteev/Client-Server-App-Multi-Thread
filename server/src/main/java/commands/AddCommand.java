package commands;

import dto.PersonDto;
import interaction.Request;
import interaction.Response;
import managers.DaoManager;


public class AddCommand extends AbstractCommand {

    public AddCommand() {
        super("add", "add a new element to the collection.", "{element}");
    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {
        PersonDto dto = (PersonDto) req.getBody();
        dto.setOwnerId(req.getAuthorization());
        daoManager.personDao.create(dto);
        return new Response<>(Response.Status.COMPLETED, "", "Person has successfully added");
    }
}
