package dao;

import models.User;

import java.sql.*;

public class UsersDao {
    private final Connection connection;
    private final String getUserByLoginQuery = "SELECT id,login, password, prefix, suffix FROM Users WHERE login = ? LIMIT 1;";
    private final String createUserQuery = "INSERT INTO Users(login, password, prefix, suffix) VALUES(?,?,?,?);";

    public UsersDao(Connection connection) {
        this.connection = connection;
    }

    public User getByLogin(String login) {
        try {
            PreparedStatement statement = connection.prepareStatement(getUserByLoginQuery);
            statement.setString(1, login);
            ResultSet userFromDb = statement.executeQuery();

            if (userFromDb.next()) {
                int id = userFromDb.getInt("id");
                String login_ = userFromDb.getString("login");
                String hashedPassword = userFromDb.getString("password");
                String prefix = userFromDb.getString("prefix");
                String suffix = userFromDb.getString("suffix");
                return new User(id,login_, hashedPassword, prefix, suffix);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void create(String login, String hashedPassword, String prefix, String suffix) {
        try {
            PreparedStatement statement = connection.prepareStatement(createUserQuery);
            statement.setString(1, login);
            statement.setString(2, hashedPassword);
            statement.setString(3, prefix);
            statement.setString(4, suffix);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
