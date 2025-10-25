package ru.kpfu.itis.semester_work.dao;

import ru.kpfu.itis.semester_work.model.Comment;
import ru.kpfu.itis.semester_work.model.Event;
import ru.kpfu.itis.semester_work.model.User;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO implements DAO<Comment> {
    private final Connection connection;

    public CommentDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Comment create(Comment comment) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COMMENT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, comment.getText());
            preparedStatement.setInt(2, comment.getUser().getId());
            preparedStatement.setInt(3, comment.getEvent().getId());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comment.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating comment", e);
        }
        return comment;
    }

    @Override
    public Comment get(int id) {
        Comment comment = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_COMMENT_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (!resultSet.getBoolean("is_deleted")) {
                    comment = mapToComment(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving comment by ID", e);

        }
        return comment;
    }

    @Override
    public void update(Comment x) {

    }

    @Override
    public void delete(Comment comment) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMMENT_SQL)) {
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, comment.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating comment", e);
        }
    }

    @Override
    public List<Comment> getAll() {
        List<Comment> comments = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_COMMENTS_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                if (!resultSet.getBoolean("is_deleted")) {
                    comments.add(mapToComment(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting comment", e);
        }
        return comments;
    }

    public List<Comment> findCommentsByEventId(Integer eventId) {
        List<Comment> comments = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COMMENTS_BY_EVENT_ID_SQL)) {
            preparedStatement.setInt(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (!resultSet.getBoolean("is_deleted")) {
                    comments.add(mapToComment(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all comments", e);
        }
        return comments;
    }

    private Comment mapToComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getInt("id"));
        comment.setText(resultSet.getString("text"));
        comment.setUser(new User(resultSet.getInt("user_id"), resultSet.getString("user_username")));
        comment.setEvent(new Event(resultSet.getInt("event_id"), resultSet.getString("event_title")));
        comment.setDate(resultSet.getTimestamp("time"));
        return comment;
    }

    private static final String INSERT_COMMENT_SQL = "INSERT INTO comment (text, user_id, event_id) VALUES (?, ?, ?)";
    private static final String GET_COMMENT_BY_ID_SQL = "SELECT comment.*, users.username as user_username, event.title as event_title FROM comment join users on comment.user_id = users.id join event on comment.event_id = event.id WHERE comment.id = ?";
    private static final String UPDATE_COMMENT_SQL = "UPDATE comment SET text = ?, user_id = ?, event_id = ? WHERE id = ?";
    private static final String DELETE_COMMENT_SQL = "UPDATE comment SET is_deleted = ? WHERE id = ?";
    private static final String GET_ALL_COMMENTS_SQL = "SELECT comment.*, users.username as user_username, event.title as event_title FROM comment join users on comment.user_id = users.id join event on comment.event_id = event.id";
    private static final String SELECT_COMMENTS_BY_EVENT_ID_SQL = "SELECT comment.*, users.username as user_username, event.title as event_title FROM comment join users on comment.user_id = users.id join event on comment.event_id = event.id WHERE event_id = ?";
}
