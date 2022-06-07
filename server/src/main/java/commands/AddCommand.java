//package commands;
//
//import dto.PersonDto;
//import interaction.Request;
//import interaction.Response;
//import managers.LinkedListCollectionManager;
//import models.Person;
//import utils.PersonFormatter;
//
//
//public class AddCommand extends AbstractCommand {
//    LinkedListCollectionManager collectionManager;
//
//    public AddCommand(LinkedListCollectionManager collectionManager) {
//        super("add", "add a new element to the collection.", "{element}");
//        this.collectionManager = collectionManager;
//    }
//
//    @Override
//    public Response execute(Request req) {
//        PersonDto dto = (PersonDto) req.getBody();
//        dto.setOwnerId(req.getAuthorization());
////        Person person = new Person(
////                1,
////                dto.getName(),
////                dto.getCoordinatesDto(),
////                dto.getHeight(),
////                dto.getWeight(),
////                dto.getEyesColor(),
////                dto.getHairsColor(),
////                dto.getLocationDto(),
////                1
////        );
//
//        try {
////            collectionManager.add(person);
//            System.out.println("Person has successfully added");
//            return new Response<>(Response.Status.COMPLETED, "", PersonFormatter.format(person) + "\nPerson has successfully added");
//        } catch (SecurityException e) {
//            return new Response<>(Response.Status.FAILURE, "Person id must be unique");
//        }
//    }
//}
