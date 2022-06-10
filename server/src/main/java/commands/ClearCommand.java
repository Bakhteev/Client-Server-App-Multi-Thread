package commands;

import interaction.Request;
import interaction.Response;
import managers.DaoManager;

public class ClearCommand extends AbstractCommand {

    public ClearCommand() {
        super("clear", "clears collection", "");
    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() != null) {
                throw new IllegalArgumentException("Using of command: " + getName());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return new Response(Response.Status.FAILURE, e.getMessage());
        }

        if (daoManager.personDao.getNumberOfPersonsByOwnerId("1") == 0) {
            return new Response(Response.Status.FAILURE, "You don't have any elements");
        } else {
//            collectionManager.clearCollection();
            daoManager.personDao.deleteAllPersonsByOwnerId(req.getAuthorization()+"");
//            System.out.println("Collection has successfully cleared");
            return new Response(Response.Status.COMPLETED, "", "You have successfully deleted all of your elements");
        }
    }
}
