package models;

import lombok.Getter;

@Getter
public class User {
    final int id;
    final String login;
    final String hashedPassword;
    final String prefix;
    final String suffix;

    public User(int id, String login, String hashedPassword, String prefix, String suffix) {
        this.id = id;
        this.login = login;
        this.hashedPassword = hashedPassword;
        this.prefix = prefix;
        this.suffix = suffix;
    }
}
