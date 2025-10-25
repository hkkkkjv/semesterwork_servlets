package ru.kpfu.itis.semester_work.dao;

import ru.kpfu.itis.semester_work.model.Forum;
import ru.kpfu.itis.semester_work.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ForumDAO implements DAO<Forum> {
    private final Connection connection;

    public ForumDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Forum create(Forum forum) {
        if (forum == null) {
            throw new IllegalArgumentException("Forum cannot be null");
        }
        try (PreparedStatement statement = connection.prepareStatement(CREATE_FORUM, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, forum.getText());
            if (forum.getUser() != null) {
                statement.setInt(2, forum.getUser().getId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            statement.setInt(3, forum.getTopicId());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                forum.setId(generatedKeys.getInt(1));
            }
            if (forum.getUser() != null) {
                addUserToTopicParticipants(forum.getUser().getId(),forum.getTopicId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating forum",e );
        }
        return forum;
    }
    private void addUserToTopicParticipants(int userId,int topicId){
        try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PARTICIPANT)){
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,topicId);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error adding user to topic participant");
        }
    }
    @Override
    public Forum get(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_FORUM)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapToForum(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving forum by ID",e );
        }
        return null;
    }

    @Override
    public void update(Forum x) {

    }

    @Override
    public void delete(Forum forum) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_FORUM)) {
            statement.setInt(1, forum.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting forum",e );
        }
    }

    @Override
    public List<Forum> getAll() {
        List<Forum> forums = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_FORUM);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                forums.add(mapToForum(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all forums",e );
        }
        return forums;
    }
    public List<Forum> getForumsByTopicId(int topicId,boolean isPinned) {
        List<Forum> forums = new ArrayList<>();
        String query = isPinned ? GET_PINNED_BY_TOPIC_ID : GET_UNPINNED_BY_TOPIC_ID;
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,topicId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                forums.add(mapToForum(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return forums;
    }
    public List<Forum> getPinnedByTopicId(int topicId) {
        return getForumsByTopicId(topicId,true);
    }
    public List<Forum> getUnpinnedByTopicId(int topicId) {
        return getForumsByTopicId(topicId,false);
    }
    private Forum mapToForum(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id");
        String text = resultSet.getString("text");
        int userId = resultSet.getInt("user_id");
        Timestamp date = resultSet.getTimestamp("date");
        int topicId = resultSet.getInt("topic_id");
        boolean isPinned = resultSet.getBoolean("is_pinned");
        User user = null;
        if (userId != 0) {
            user = new User(userId, resultSet.getString("username"));
        }
        return new Forum(id,text,user,date,topicId,isPinned);
    }
    private static final String CREATE_FORUM = "INSERT INTO forums (text, user_id,topic_id) VALUES (?, ?, ?)";
    private static final String INSERT_PARTICIPANT = "INSERT INTO topic_participants (user_id, topic_id) VALUES (?, ?)";
    private static final String GET_FORUM = "SELECT forums.*,users.username FROM forums left join users on forums.user_id = users.id WHERE forums.id = ?";
    private static final String UPDATE_FORUM = "UPDATE forums SET text = ?, user_id = ?, date = ? WHERE id = ?";
    private static final String DELETE_FORUM = "DELETE FROM forums WHERE id = ?";
    private static final String GET_ALL_FORUM = "SELECT forums.* , users.username FROM forums left join users on forums.user_id=users.id";
    private static final String GET_PINNED_BY_TOPIC_ID = "SELECT forums.* , users.username FROM forums left join users on forums.user_id=users.id where forums.topic_id=? and forums.is_pinned=true order by forums.date desc";
    private static final String GET_UNPINNED_BY_TOPIC_ID = "SELECT forums.* , users.username FROM forums left join users on forums.user_id=users.id where forums.topic_id=? and forums.is_pinned=false order by forums.date desc";
    private static final String PIN_FORUM = "UPDATE forums set is_pinned=true where id=?";
    private static final String UNPIN_FORUM = "UPDATE forums set is_pinned=false where id=?";


    public void pinForum(int forumId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(PIN_FORUM)){
            preparedStatement.setInt(1,forumId);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error pinning forum post",e);
        }
    }
    public void unpinForum(int forumId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UNPIN_FORUM)){
            preparedStatement.setInt(1,forumId);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error unpinning forum post",e);
        }
    }
}
