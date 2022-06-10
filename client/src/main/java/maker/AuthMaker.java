package maker;

import validators.AuthValidator;
import workers.ConsoleWorker;

import java.io.Console;
import java.util.concurrent.ExecutionException;

public class AuthMaker {
    Console console;
    public AuthMaker(Console console) {
        this.console = console;
    }

    public String askLogin(){
        String login;
        while (true) {
            try {

            login = console.readLine("Enter login: ").trim();
            AuthValidator.validateLogin(login);
            return login;
            }catch (Throwable e){
                System.out.println(e);
            }
        }
    }

    public String askPassword(){
        char[] passwordFromChar;
        StringBuilder password = new StringBuilder();
        while (true) {
            try {
                passwordFromChar = console.readPassword("Enter password: ");
                for (int i = 0; i < passwordFromChar.length; i++){
                    password.append(passwordFromChar[i]);
                }
                AuthValidator.validatePassword(password.toString());
                return password.toString();
            }catch (Throwable e){
                System.out.println(e);
            }
        }
    }


}
