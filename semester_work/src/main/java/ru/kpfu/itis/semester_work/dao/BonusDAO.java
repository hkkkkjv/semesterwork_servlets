package ru.kpfu.itis.semester_work.dao;

import ru.kpfu.itis.semester_work.model.BonusPoints;
import ru.kpfu.itis.semester_work.model.Event;
import ru.kpfu.itis.semester_work.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BonusDAO implements DAO<BonusPoints> {
    private final Connection connection;

    public BonusDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public BonusPoints create(BonusPoints bonusPoints) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BONUS_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, bonusPoints.getEvent().getId());
            preparedStatement.setInt(2, bonusPoints.getStudent().getId());
            preparedStatement.setInt(3, bonusPoints.getPoints());
            preparedStatement.setTimestamp(4, new Timestamp(bonusPoints.getExpirationDate().getTime()));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                bonusPoints.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating BonusPoints");
        }
        return bonusPoints;
    }

    @Override
    public BonusPoints get(int x) throws SQLException {
        return null;
    }

    public List<BonusPoints> getActiveBonusPointsByStudentId(int userId) {
        return getBonusPoints(GET_BONUSES_BY_USER_ID_SQL,userId);
    }

    public int getTotalActiveBonusPointsByStudentId(int userId) {
        int totalPoints = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_BONUSES_BY_USER_ID_SQL)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalPoints = resultSet.getInt("total_points");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving bonus points",e);
        }
        return totalPoints;
    }

    @Override
    public void update(BonusPoints x) {

    }

    @Override
    public void delete(BonusPoints x) {

    }

    @Override
    public List<BonusPoints> getAll() {
        return null;
    }

    public List<BonusPoints> getAllBonusPointsByTeacherId(int teacherId) {
        return getBonusPoints(GET_BONUSES_BY_TEACHER_ID_SQL,teacherId);
    }
    public List<BonusPoints> getBonusPoints(String sql, int id) {
        List<BonusPoints> bonusPoints = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bonusPoints.add(mapToBonusPoints(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving bonus points",e);
        }
        return bonusPoints;
    }

    private BonusPoints mapToBonusPoints(ResultSet resultSet) throws SQLException {
        BonusPoints bonusPoints = new BonusPoints();
        try {
            bonusPoints.setId(resultSet.getInt("id"));
            Event event = new Event(resultSet.getInt("event_id"),
                    resultSet.getString("event_title"),
                    resultSet.getString("event_description"));
            User student = new User(resultSet.getInt("user_id"), resultSet.getString("user_username"));
            bonusPoints.setEvent(event);
            bonusPoints.setStudent(student);
            bonusPoints.setPoints(resultSet.getInt("points"));
            bonusPoints.setDate(resultSet.getTimestamp("date"));
            bonusPoints.setExpirationDate(resultSet.getTimestamp("expiration_date"));
        }catch (SQLException e){
            throw new RuntimeException("Error mapping ResultSet to BonusPoints",e);
        }
        return bonusPoints;
    }

    private static final String INSERT_BONUS_SQL = "INSERT INTO bonus_points (event_id,user_id,points,expiration_date) VALUES (?, ?, ?, ?)";
    private static final String GET_BONUSES_BY_USER_ID_SQL = "select bp.*,e.id as event_id, e.title as event_title,e.description as event_description, u.id as user_id,u.username as user_username from bonus_points bp join public.event e on e.id = bp.event_id join public.users u on u.id = bp.user_id where u.id = ? and  bp.expiration_date>current_timestamp";
    private static final String GET_BONUSES_BY_TEACHER_ID_SQL = "select bp.*,e.id as event_id, e.title as event_title,e.description as event_description, u.id as user_id,u.username as user_username from bonus_points bp join event e on e.id = bp.event_id join users u on u.id = bp.user_id where e.created_by = ?";
    private static final String GET_TOTAL_BONUSES_BY_USER_ID_SQL = "select sum(bp.points) as total_points from bonus_points bp where bp.user_id = ? and  bp.expiration_date>current_timestamp";
}
