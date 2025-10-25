package ru.kpfu.itis.semester_work.dao;

import ru.kpfu.itis.semester_work.model.Role;
import ru.kpfu.itis.semester_work.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User> {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User create(User user) {
        try (PreparedStatement statement = connection.prepareStatement(ADD_USER)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getRole().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user", e);
        }
        return user;
    }

    @Override
    public User get(int id) {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = mapToUser(resultSet);
            }
        }catch (SQLException e){
            throw new RuntimeException("Error retrieving user by id", e);
        }
        return user;
    }

    public User get(String username, String password) {
        User user = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users where username = ? and password = ? ");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = mapToUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user by username and password", e);
        }
        return user;
    }
    public User getByUsername(String username) {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_USERNAME)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user=mapToUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user by username", e);
        }
        return user;
    }
    public User getByEmail(String email) {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = mapToUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user by email", e);
        }
        return user;
    }
    @Override
    public void update(User user) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getAbout());
            statement.setInt(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    @Override
    public void delete(User user) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_USERS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapToUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public List<User> getAllTeachers() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_TEACHERS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapToUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    private User mapToUser(ResultSet resultSet) throws SQLException{
        User user = new User(
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role")),
                resultSet.getString("about")
        );
        user.setId(resultSet.getInt("id"));
        return user;
    }
    final private static String ADD_USER = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
    final private static String FIND_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    final private static String FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    final private static String FIND_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    final private static String FIND_USERS = "SELECT * FROM users";
    final private static String FIND_TEACHERS = "SELECT * FROM users where role = 'TEACHER'";
    final private static String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    final private static String UPDATE_USER = "UPDATE users SET username = ?, email = ?, password = ?, about = ? WHERE id = ?";

}
