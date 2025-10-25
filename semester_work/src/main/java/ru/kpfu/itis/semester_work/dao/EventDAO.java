package ru.kpfu.itis.semester_work.dao;

import ru.kpfu.itis.semester_work.model.Category;
import ru.kpfu.itis.semester_work.model.Event;
import ru.kpfu.itis.semester_work.model.User;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDAO implements DAO<Event> {
    private final Connection connection;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public EventDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Event create(Event event) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EVENT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, event.getTitle());
            preparedStatement.setString(2, event.getDescription());
            preparedStatement.setString(3, event.getLocation());
            preparedStatement.setTimestamp(4, event.getTime());
            preparedStatement.setInt(5, event.getSeatCount());
            preparedStatement.setInt(6, event.getCategory().getId());
            preparedStatement.setInt(7, event.getCreatedBy().getId());
            preparedStatement.setString(8, event.getImagePath());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                event.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating event", e);
        }
        return event;
    }

    @Override
    public Event get(int id) {
        Event event = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_EVENT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                event = mapToEvent(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving event by ID", e);
        }
        return event;
    }

    public List<Event> getEventsByUserId(int id) {
        List<Event> events = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_EVENT_BY_USER_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    events.add(mapToEvent(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving events by user ID");
        }
        return events;
    }

    @Override
    public void update(Event event) {

    }

    @Override
    public void delete(Event event) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EVENT_SQL)) {
            preparedStatement.setInt(1, event.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting event", e);
        }
    }

    @Override
    public List<Event> getAll() {
        List<Event> events = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_EVENTS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                events.add(mapToEvent(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all events", e);
        }
        return events;
    }

    public List<Event> getUpcomingEvents() {
        List<Event> events = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_UPCOMING_EVENTS)) {
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    events.add(mapToEvent(resultSet));
                }
            }
        } catch (
                SQLException e) {
            throw new RuntimeException("Error retrieving upcoming events", e);
        }
        return events;
    }

    private Event mapToEvent(ResultSet resultSet) throws SQLException {
        Event event = new Event();
        event.setId(resultSet.getInt("id"));
        event.setTitle(resultSet.getString("title"));
        event.setDescription(resultSet.getString("description"));
        event.setLocation(resultSet.getString("location"));
        event.setTime(resultSet.getTimestamp("time"));
        event.setSeatCount(resultSet.getInt("seat_count"));
        event.setImagePath(resultSet.getString("image_path"));
        Category category = new Category();
        category.setId(resultSet.getInt("category_id"));
        category.setName(resultSet.getString("category_name"));
        event.setCategory(category);
        User user = new User();
        user.setId(resultSet.getInt("created_by"));
        user.setUsername(resultSet.getString("created_by_username"));
        event.setCreatedBy(user);
        return event;
    }

    public List<Event> searchEvents(String query, String categoryId, String startDate, String endDate, String createdBy,String sortOrder) {
        List<Event> events = new ArrayList<>();
        StringBuilder search = new StringBuilder("SELECT event.*,category.name as category_name,users.username as created_by_username " +
                "FROM event join category on event.category_id=category.id " +
                "join users on event.created_by=users.id WHERE 1=1 ");
        if (query != null && !query.isEmpty()) {
            search.append(" and (lower(event.title) like lower(?) or lower(event.description) like lower(?))");
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            search.append(" and event.category_id = ?");
        }
        if (startDate != null && !startDate.isEmpty()) {
            search.append(" and event.time >= ?");
        }
        if (endDate != null && !endDate.isEmpty()) {
            search.append(" and event.time <= ?");
        }
        if (createdBy != null && !createdBy.isEmpty()) {
            search.append(" and event.created_by = ?");
        }
        if (sortOrder!=null&&!sortOrder.isEmpty()){
            switch (sortOrder){
                case "dateAsc":
                    search.append(" order by event.time asc");
                    break;
                case "dateDessc":
                    search.append(" order by event.time desc");
                    break;
                case "titleAsc":
                    search.append(" order by event.title asc");
                    break;
                case "titleDesc":
                    search.append(" order by event.title desc");
                    break;
                default:
                    break;
            }
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(search.toString())){
            int index = 1;
            if (query != null && !query.isEmpty()) {
                preparedStatement.setString(index++,"%"+query+"%");
                preparedStatement.setString(index++,"%"+query+"%");
            }
            if (categoryId != null && !categoryId.isEmpty()) {
                preparedStatement.setInt(index++,Integer.parseInt(categoryId));
            }
            if (startDate != null && !startDate.isEmpty()) {
                try{
                    Date startParsedDate =  dateFormat.parse(startDate);
                    preparedStatement.setTimestamp(index++,new Timestamp(startParsedDate.getTime()));
                }catch (ParseException e){
                    throw new RuntimeException("Invalid startDate format",e);
                }
            }
            if (endDate != null && !endDate.isEmpty()) {
                try{
                    Date endParsedDate =  dateFormat.parse(endDate);
                    preparedStatement.setTimestamp(index++,new Timestamp(endParsedDate.getTime()));
                }catch (ParseException e){
                    throw new RuntimeException("Invalid endDate format",e);
                }
            }
            if (createdBy != null && !createdBy.isEmpty()) {
                preparedStatement.setInt(index++,Integer.parseInt(createdBy));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                events.add(mapToEvent(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw  new RuntimeException("Error retrieving events by search query");
        }
        return events;
    }

    private static final String INSERT_EVENT_SQL = "INSERT INTO event (title, description, location, time, seat_count, category_id,created_by,image_path) VALUES (?, ?, ?, ?, ?, ?,?,?)";
    private static final String GET_EVENT_BY_ID = "SELECT event.*,category.name as category_name,users.username as created_by_username FROM event join category on event.category_id=category.id join users on event.created_by=users.id WHERE event.id = ?";
    private static final String GET_EVENT_BY_USER_ID = "SELECT event.*,category.name as category_name,users.username as created_by_username FROM event join category on event.category_id=category.id join users on event.created_by=users.id WHERE created_by = ? order by event.time desc ";
    private static final String GET_ALL_EVENTS = "SELECT event.*,category.name as category_name,users.username as created_by_username FROM event join category on event.category_id=category.id join users on event.created_by=users.id ORDER BY event.time DESC";
    private static final String GET_UPCOMING_EVENTS = "SELECT event.*,category.name as category_name,users.username as created_by_username FROM event join category on event.category_id=category.id join users on event.created_by=users.id where event.time> ?  ORDER BY event.time DESC limit 5";
    private static final String UPDATE_EVENT_SQL = "UPDATE event SET title = ?, description = ?, location = ?, time = ?, seat_count = ?, category_id = ? WHERE id = ?";
    private static final String DELETE_EVENT_SQL = "DELETE FROM event WHERE id = ?";


}
