package ru.kpfu.itis.semester_work.dao;

import ru.kpfu.itis.semester_work.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO implements DAO<Registration> {
    private final Connection connection;

    public RegistrationDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Registration create(Registration registration) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_REGISTRATION, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, registration.getUser().getId());
            preparedStatement.setInt(2, registration.getEvent().getId());
            preparedStatement.setString(3, registration.getStatus().name());
            preparedStatement.setString(4, registration.getComment());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    registration.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return registration;
    }

    @Override
    public Registration get(int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REGISTRATION_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToRegistration(resultSet);
                }
            }
        }
        return null;
    }

    public int getActiveRegistrationCount(int eventId) {
        int count = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COUNT_ACTIVE_REGISTRATIONS)) {
            preparedStatement.setInt(1, eventId);
            preparedStatement.setString(2, Status.REGISTERED.toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public Registration getByEventAndUser(int eventId, int userId) {
        Registration registration = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REGISTRATION_BY_EVENT_AND_USER)) {
            preparedStatement.setInt(1, eventId);
            preparedStatement.setInt(2, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToRegistration(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving registration by event and user", e);
        }
        return registration;
    }

    public List<Registration> getRegistrationsByUserId(int userId) {
        List<Registration> registrations = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REGISTRATION_BY_USER_ID)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    registrations.add(mapToRegistration(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving registrations by user ID", e);
        }
        return registrations;
    }

    @Override
    public void update(Registration registration) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_REGISTRATION)) {
            preparedStatement.setInt(1, registration.getUser().getId());
            preparedStatement.setInt(2, registration.getEvent().getId());
            preparedStatement.setString(3, registration.getStatus().name());
            preparedStatement.setString(4, registration.getComment());
            preparedStatement.setInt(5, registration.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating registration: " + e.getMessage());
        }
    }

    public void cancelRegistration(Integer id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATUS)) {
            preparedStatement.setString(1, Status.CANCELLED.toString());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating registration" + e.getMessage());
        }

    }

    public List<User> getUsersByEventId(int eventId) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_BY_EVENT_ID)) {
            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User(resultSet.getInt("user_id"), resultSet.getString("user_username"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving users by event ID", e);
        }
        return users;
    }

    public List<Registration> getRegistrationByEventId(int eventId) {
        List<Registration> registrations = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REGISTRATION_BY_EVENT_ID)) {
            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    registrations.add(mapToRegistration(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving registration by event ID");
        }
        return registrations;
    }

    private Registration mapToRegistration(ResultSet resultSet) throws SQLException {
        Registration registration;
        User user = new User(resultSet.getInt("user_id"), resultSet.getString("user_username"));
        Event event = new Event(resultSet.getInt("event_id"),
                resultSet.getString("event_title"),
                resultSet.getString("event_description"),
                resultSet.getString("event_location"),
                resultSet.getTimestamp("event_time"),
                resultSet.getInt("seat_count"),
                new Category(resultSet.getInt("category_id"),
                        resultSet.getString("category_name")),
                new User(resultSet.getInt("created_by"),
                        resultSet.getString("created_by_username")),
                resultSet.getString("event_image_path"));
        registration = new Registration(
                resultSet.getInt("registration_id"),
                user,
                event,
                resultSet.getTimestamp("registration_time"),
                Status.valueOf(resultSet.getString("status")),
                resultSet.getString("comment")
        );
        return registration;
    }

    @Override
    public void delete(Registration x) {

    }

    @Override
    public List<Registration> getAll() {
        return null;
    }

    private static final String INSERT_REGISTRATION = "INSERT INTO registration (user_id, event_id, status, comment) VALUES (?, ?, ?, ?)";
    private static final String SELECT_REGISTRATION_BY_ID = "SELECT r.id AS registration_id, r.user_id, r.registration_time, r.status, r.comment, u.username AS user_username, e.id AS event_id, e.title AS event_title, e.description AS event_description, e.location AS event_location, e.time AS event_time, e.seat_count, e.category_id, e.created_by, e.image_path AS event_image_path, c.name AS category_name, cb.username AS created_by_username FROM registration r JOIN users u ON r.user_id = u.id JOIN event e ON r.event_id = e.id JOIN users cb ON e.created_by = cb.id LEFT JOIN category c ON e.category_id = c.id WHERE r.id = ?";
    private static final String SELECT_REGISTRATION_BY_USER_ID = "SELECT r.id AS registration_id, r.user_id, r.registration_time, r.status, r.comment, u.username AS user_username, e.id AS event_id, e.title AS event_title, e.description AS event_description, e.location AS event_location, e.time AS event_time, e.seat_count, e.category_id, e.created_by, e.image_path AS event_image_path, c.name AS category_name, cb.username AS created_by_username FROM registration r JOIN users u ON r.user_id = u.id JOIN event e ON r.event_id = e.id JOIN users cb ON e.created_by = cb.id LEFT JOIN category c ON e.category_id = c.id WHERE r.user_id = ? AND r.status <> 'CANCELLED'";
    private static final String SELECT_REGISTRATION_BY_EVENT_ID = "SELECT r.id AS registration_id, r.user_id, r.registration_time, r.status, r.comment, u.username AS user_username, e.id AS event_id, e.title AS event_title, e.description AS event_description, e.location AS event_location, e.time AS event_time, e.seat_count, e.category_id, e.created_by, e.image_path AS event_image_path, c.name AS category_name, cb.username AS created_by_username FROM registration r JOIN users u ON r.user_id = u.id JOIN event e ON r.event_id = e.id JOIN users cb ON e.created_by = cb.id LEFT JOIN category c ON e.category_id = c.id WHERE r.event_id = ? AND r.status <> 'CANCELLED'";
    private static final String SELECT_REGISTRATION_BY_EVENT_AND_USER = "SELECT r.id AS registration_id, r.user_id, r.registration_time, r.status, r.comment, u.username AS user_username, e.id AS event_id, e.title AS event_title, e.description AS event_description, e.location AS event_location, e.time AS event_time, e.seat_count, e.category_id, e.created_by, e.image_path AS event_image_path, c.name AS category_name, cb.username AS created_by_username FROM registration r JOIN users u ON r.user_id = u.id JOIN event e ON r.event_id = e.id JOIN users cb ON e.created_by = cb.id LEFT JOIN category c ON e.category_id = c.id WHERE r.event_id = ? AND r.user_id = ?";
    private static final String UPDATE_STATUS = "UPDATE registration SET status = ? WHERE id = ?";
    private static final String UPDATE_REGISTRATION = "UPDATE registration SET user_id = ?, event_id = ?, status = ?, comment = ? WHERE id = ?";
    private static final String GET_COUNT_ACTIVE_REGISTRATIONS = "SELECT COUNT(*) FROM registration WHERE event_id = ? AND status = ?";
    private static final String SELECT_USERS_BY_EVENT_ID = "SELECT u.id AS user_id, u.username AS user_username FROM registration r JOIN users u ON r.user_id = u.id  WHERE r.event_id = ? AND r.status <> 'CANCELLED'";


}
