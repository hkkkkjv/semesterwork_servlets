package ru.kpfu.itis.semester_work.dao;

import ru.kpfu.itis.semester_work.model.Topic;
import ru.kpfu.itis.semester_work.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicDAO implements DAO<Topic>{
    private final Connection connection;

    public TopicDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Topic create(Topic topic) {
        if (topic==null){
            throw new IllegalArgumentException("Topic cannot be null");
        }
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TOPIC, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1,topic.getTitle());
            statement.setString(2,topic.getDescription());
            statement.setInt(3,topic.getCreatedBy().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()){
                topic.setId(resultSet.getInt(1));
            }
        }catch (SQLException e){
            throw new RuntimeException("Error creating topic", e);
        }
        return topic;
    }

    @Override
    public Topic get(int id){
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_TOPIC)){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Topic topic= mapToTopic(resultSet,id);
                topic.setParticipants(getParticipantsByTopicId(id));
                return topic;
            }
        }catch (SQLException e){
            throw  new RuntimeException("Error retrieving topic by ID",e);
        }
        return null;
    }
    private List<User> getParticipantsByTopicId(int topicId) {
        List<User> participant = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PARTICIPANTS)){
            preparedStatement.setInt(1,topicId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int userId = resultSet.getInt("id");
                String username = resultSet.getString("username");
                participant.add(new User(userId,username));
            }
        }catch (SQLException e){
            throw new RuntimeException("Error retrieving topic by ID",e);
        }
        return participant;
    }
    @Override
    public void update(Topic topic) {
        if (topic==null){
            throw new IllegalArgumentException("Topic cannot be null");
        }
        try(PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TOPIC)){
            preparedStatement.setString(1,topic.getTitle());
            preparedStatement.setString(2,topic.getDescription());
            preparedStatement.setInt(3,topic.getCreatedBy().getId());
            preparedStatement.setTimestamp(4,topic.getUpdatedAt());
            preparedStatement.setInt(5,topic.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error updating topic",e);
        }
    }

    @Override
    public void delete(Topic topic) {

    }

    @Override
    public List<Topic> getAll() {
        List<Topic> topics = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_TOPICS);
            ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()){
                topics.add(mapToTopic(resultSet, resultSet.getInt("id")));            }
        }catch (SQLException e){
            throw new RuntimeException("Error retrieving all topics",e);
        }
        return topics;
    }
    public List<Topic> getNewTopics() {
        List<Topic> topics = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_NEW_TOPICS);
            ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()){
                topics.add(mapToTopic(resultSet, resultSet.getInt("id")));
            }
        }catch (SQLException e){
            throw new RuntimeException("Error retrieving new topics",e);
        }
        return topics;
    }
    private Topic mapToTopic(ResultSet resultSet,int id)throws SQLException{
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        User createdBy = new User(resultSet.getInt("created_by"), resultSet.getString("username"));
        return new Topic(id, title, description, createdAt, updatedAt, createdBy);
    }
    private static final String CREATE_TOPIC = "INSERT INTO topic (title, description, created_by) VALUES (?, ?, ?)";
    private static final String GET_TOPIC = "SELECT t.*, u.username FROM topic t LEFT JOIN users u ON t.created_by = u.id WHERE t.id = ?";
    private static final String GET_PARTICIPANTS = "select u.id,u.username from users u join (select user_id, min(topic_participants.joined_at) as earliest_joined from topic_participants where topic_id = ? group by user_id) tp on u.id = tp.user_id order by tp.earliest_joined limit 5";
    private static final String UPDATE_TOPIC = "UPDATE topic SET title = ?, description = ?, created_by = ?, updated_at = ? WHERE id = ?";
    private static final String DELETE_TOPIC = "DELETE FROM topic WHERE id = ?";
    private static final String GET_ALL_TOPICS = "SELECT t.*, u.username FROM topic t LEFT JOIN users u ON t.created_by = u.id order by created_at desc ";
    private static final String GET_NEW_TOPICS = "SELECT t.*, u.username FROM topic t LEFT JOIN users u ON t.created_by = u.id order by created_at desc limit 5";

}
