//package commands;
//
//import interaction.Request;
//import interaction.Response;
//import managers.LinkedListCollectionManager;
//import utils.FileWorker;
//import utils.JSONParser;
//
//import java.io.IOException;
//
//// TODO: ADD LOGGER
//
//public class SaveCommand extends AbstractCommand {
//    FileWorker fileWorker;
//    LinkedListCollectionManager collectionManager;
//
//    public SaveCommand(LinkedListCollectionManager collectionManager, FileWorker fileWorker) {
//        super("save", "saves collection data to file", "");
//        this.fileWorker = fileWorker;
//        this.collectionManager = collectionManager;
//    }
//
//    @Override
//    public Response execute(Request req) {
//        try {
//            fileWorker.saveFile(JSONParser.toJSON(collectionManager.getCollection()));
//            collectionManager.setLastSaveTime();
//            System.out.println("Collection successfully saved");
//            return null;
//        } catch (SecurityException exception) {
//            System.out.println("\u001B[31mFile permission error!\u001B[0m");
//            return null;
//        } catch (IOException exception) {
//            System.out.println("\u001B[31mFailed to save data to file!\u001B[0m");
//            return null;
//        }
//    }
//}
