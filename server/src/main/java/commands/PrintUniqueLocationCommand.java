package commands;
import interaction.Request;
import interaction.Response;
import managers.DaoManager;
import models.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintUniqueLocationCommand extends AbstractCommand {


    public PrintUniqueLocationCommand() {
        super("print_unique_location", "display the unique values of the location field of all elements in the collection.", "");
    }

    @Override
    public Response<?> execute(Request req, DaoManager daoManager) {
        try {
            if (req.getParams() != null) {
                throw new IllegalArgumentException("Using of command: " + getName());
            }
            if (daoManager.personDao.getNumberOfPersons() == 0) {
                throw new IllegalStateException("Collection is empty");
            }
        } catch (IllegalArgumentException e) {
            return new Response<>(Response.Status.FAILURE, e.getMessage());
        }
        HashMap<String, Integer> map = countLocations(daoManager);
        StringBuilder locationNamesToShow = new StringBuilder();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                locationNamesToShow.append(entry.getKey() + "\n");
            }
        }
        return new Response<>(Response.Status.COMPLETED, "",
                locationNamesToShow.toString().length() == 0 ? "No unique Location" : locationNamesToShow.toString());
    }

    private HashMap<String, Integer> countLocations(DaoManager daoManager) {
        HashMap<String, Integer> map = new HashMap<>();
        List<Location> listOfLocations = daoManager.locationDao.getAll();
        for (Location element : listOfLocations) {
            String locationName = element.getName();
            if (map.containsKey(locationName)) {
                map.put(locationName, map.get(locationName) + 1);
            } else {
                map.put(locationName, 1);
            }
        }
        return map;
    }
}
