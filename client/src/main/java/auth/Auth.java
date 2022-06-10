package auth;

import client.Client;
import dto.UserDto;
import interaction.Request;
import interaction.Response;
import lombok.Getter;
import maker.AuthMaker;
import workers.ConsoleWorker;

import java.io.IOException;

public class Auth {
    @Getter
    public boolean isAuthenticated = false;
    public static Integer authHeader;
    AuthMaker maker;

    public Auth(AuthMaker maker) {
        this.maker = maker;
    }

    public void login() {
        String login = "";
        String password = "";
        while (!isAuthenticated) {
            login = maker.askLogin();
            password = maker.askPassword();
            try {
                Client.sendRequest(new Request<>("login", "", new UserDto(login, password), authHeader));
                Response res = Client.getResponse();
                if (res.getStatus() == Response.Status.FAILURE) {
                    ConsoleWorker.printError(res.getMessage());
                } else {
                    ConsoleWorker.println(res.getMessage());
                    setAuthenticated();
                    authHeader = (int) res.getBody();
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void register() {
        String login = "";
        String password = "";
        while (true) {
            login = maker.askLogin();
            password = maker.askPassword();
            try {
                Client.sendRequest(new Request<>("registration", "", new UserDto(login, password),authHeader));
                Response res = Client.getResponse();
                if (res.getStatus() == Response.Status.FAILURE) {
                    ConsoleWorker.printError(res.getMessage());
                } else {
                    ConsoleWorker.println(res.getMessage());
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAuthenticated() {
        isAuthenticated = !this.isAuthenticated;
    }
}
