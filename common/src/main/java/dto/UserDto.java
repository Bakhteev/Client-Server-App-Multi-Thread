package dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserDto implements Serializable {
    private static final long serialVersionUID = 9196440416857469613L;
    private  String login;
    private  String password;

    public UserDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
