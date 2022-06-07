package auth;

import client.Client;
import dto.UserDto;
import interaction.Request;
import interaction.Response;
import lombok.Getter;
import maker.AuthMaker;
import managers.ClientCommandManager;
import workers.ConsoleWorker;

import java.io.IOException;

public class Auth {
    @Getter
    boolean isAuthenticated = false;
    AuthMaker maker;

    public Auth(AuthMaker maker) {
        this.maker = maker;
    }

    public void login() {
        String login = "";
        String password = "";
        while (isAuthenticated) {
            login = maker.askLogin();
            password = maker.askPassword();
            try {
                Client.sendRequest(new Request<>("login", "", new UserDto(login, password)));
                Response res = Client.getResponse();
                if (res.getStatus() == Response.Status.FAILURE) {
                    ConsoleWorker.printError(res.getMessage());
                } else {
                    ConsoleWorker.println(res.getMessage());
                    setAuthenticated();
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
                Client.sendRequest(new Request<>("registration", "", new UserDto(login, password)));
                Response res = Client.getResponse();
                if (res.getStatus() == Response.Status.FAILURE) {
                    ConsoleWorker.printError(res.getMessage());
                } else {
                    ConsoleWorker.println(res.getMessage());
                    setAuthenticated();
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
