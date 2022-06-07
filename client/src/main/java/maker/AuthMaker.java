package maker;

import validators.AuthValidator;

import java.io.Console;

public class AuthMaker {
    Console console;
    public AuthMaker(Console console) {
        this.console = console;
    }

    public String askLogin(){
        String login;
        while (true) {
            login = console.readLine().trim();
            AuthValidator.validateLogin(login);
            return login;
        }
    }

    public String askPassword(){
        String password;
        while (true) {
            password = console.readLine().trim();
            AuthValidator.validatePassword(password);
            return password;
        }
    }
}
