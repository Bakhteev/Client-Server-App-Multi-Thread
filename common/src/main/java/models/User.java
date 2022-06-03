package models;

import lombok.Getter;

@Getter
public class User {
    final String login;
    final String hashedPassword;
    final String prefix;
    final String suffix;

    public User(String login, String hashedPassword, String prefix, String suffix) {
        this.login = login;
        this.hashedPassword = hashedPassword;
        this.prefix = prefix;
        this.suffix = suffix;
    }
}
