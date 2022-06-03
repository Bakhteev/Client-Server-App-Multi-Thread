package dto;

import lombok.Getter;

@Getter
public class UserDto {
    private final String login;
    private final String password;

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
