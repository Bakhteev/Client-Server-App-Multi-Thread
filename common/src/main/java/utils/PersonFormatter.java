package utils;

import models.Person;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class PersonFormatter {

    public static String format(Person person) {
        return "\n\nID: " + person.getId() + "\n" +
                "Name: " + person.getName() + "\n" +
                "Creation date: " + person.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n" +
                "Coordinates: " + "\n" +
                "   x: " + person.getCoordinates().getX() + "\n" +
                "   y: " + person.getCoordinates().getY() + "\n" +
                "Height: " + person.getHeight() + "\n" +
                "Weight: " + person.getWeight() + "\n" +
                "Eyes' color: " + person.getEyesColor().toString() + "\n" +
                "Hairs' color: " + person.getHairsColor().toString() + "\n" +
                "Location: " + "\n" +
                "   x: " + person.getLocation().getX() + "\n" +
                "   y: " + person.getLocation().getY() + "\n" +
                "   Z: " + person.getLocation().getZ() + "\n" +
                "   Location name: " + person.getLocation().getName() + "\n" +
                "Owner id: " + person.getOwnerId() + "\n";
    }

    public static String formatCollection(List<Person> collection) {
        StringBuilder result = new StringBuilder();
        for (Person person : collection) {
            result.append(format(person));
        }
        return result.toString();
    }
}
