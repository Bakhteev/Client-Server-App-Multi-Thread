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

            login = console.readLine().trim();
            AuthValidator.validateLogin(login);
            return login;
            }catch (Throwable e){
                System.out.println(e);
            }
        }
    }

    public String askPassword(){
        String password;
        while (true) {
            try {
                password = console.readLine().trim();
                AuthValidator.validatePassword(password);
                return password;
            }catch (Throwable e){
                System.out.println(e);
            }
        }
    }


}
