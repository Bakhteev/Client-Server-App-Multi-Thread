package commands;

// TODO: ADD LOGGER

import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import managers.LinkedListCollectionManager;
import managers.UserManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InfoCommand extends AbstractCommand {
    LinkedListCollectionManager collectionManager;

    public InfoCommand(LinkedListCollectionManager collectionManager) {
        super("info", "print information about the collection to standard output.", "");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() != null)
                throw new IllegalArgumentException("Using of command: " + getName());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return new Response(Response.Status.FAILURE, e.getMessage());
        }

        String body =
                "Number of elements: " + daoManager.personDao.getNumberOfPersons() + "\n"
                        + "Number of your    elements: " + daoManager.personDao.getNumberOfPersonsByOwnerId(req.getAuthorization()+"");
        return new Response<>(Response.Status.COMPLETED, "", body);
    }
}
