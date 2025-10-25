package ru.kpfu.itis.semester_work.services;

import ru.kpfu.itis.semester_work.dao.*;
import ru.kpfu.itis.semester_work.model.*;
import ru.kpfu.itis.semester_work.util.Constant;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventService {
    private final EventDAO eventDAO;
    private final RegistrationDAO registrationDAO;
    private final CommentDAO commentDAO;
    private final CategoryDAO categoryDAO;
    private final BonusDAO bonusDAO;

    public EventService(EventDAO eventDAO, RegistrationDAO registrationDAO, CommentDAO commentDAO, CategoryDAO categoryDAO, BonusDAO bonusDAO) {
        this.eventDAO = eventDAO;
        this.registrationDAO = registrationDAO;
        this.commentDAO = commentDAO;
        this.categoryDAO = categoryDAO;
        this.bonusDAO = bonusDAO;
    }

    public Event get(int id) {
        return eventDAO.get(id);
    }

    public List<Event> getUserCreatedEvents(int userId) {
        return eventDAO.getEventsByUserId(userId);
    }

    public void registerUser(Integer eventId, Integer userId, String comment) {
        Registration existingRegistration = registrationDAO.getByEventAndUser(eventId, userId);
        if (existingRegistration != null && existingRegistration.getStatus() == Status.CANCELLED) {
            existingRegistration.setStatus(Status.REGISTERED);
            existingRegistration.setComment(comment);
            registrationDAO.update(existingRegistration);
        } else {
            Registration registration = new Registration();
            registration.setUser(new User(userId));
            registration.setEvent(new Event(eventId));
            registration.setStatus(Status.REGISTERED);
            registration.setComment(comment);
            registrationDAO.create(registration);
        }
    }

    public void addComment(Integer eventId, String text, User user) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setUser(user);
        comment.setEvent(new Event(eventId));
        commentDAO.create(comment);
    }

    public List<Comment> getCommentsForEvent(Integer eventId) {
        List<Comment> comments;
        comments = commentDAO.findCommentsByEventId(eventId);
        return comments;
    }

    public void deleteComment(Integer commentId) {
        Comment comment = commentDAO.get(commentId);
        if (comment != null) {
            commentDAO.delete(comment);
        }
    }

    public Registration isUserRegistered(int eventId, Integer userId) {
        return registrationDAO.getByEventAndUser(eventId, userId);
    }

    public void cancelRegistration(Integer registrationId) {
        registrationDAO.cancelRegistration(registrationId);
    }

    public List<Event> getAllEvents() {
        return eventDAO.getAll();
    }

    public Event addEvent(String title, String description, String location, Timestamp time, int seatCount, Category category, User user, Part filePart) throws IOException {
        String imagePath = saveUploadedFile(filePart);
        Event event = new Event(title, description, location, time, seatCount, category, user, imagePath);
        return eventDAO.create(event);
    }
    private String saveUploadedFile(Part filePart) throws IOException {
        String filename = Paths.get(filePart.getSubmittedFileName()).getFileName().toString() + System.nanoTime();
        String uploadPath = Constant.IMAGE_PATH;
        File uploads = new File(uploadPath);
        if (!uploads.exists()) {
            uploads.mkdirs();
        }
        File file = new File(uploads, filename);
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return filename;
    }
    public List<Category> getAllCategories() {
        return categoryDAO.getAll();
    }

    public int getActiveRegistrationCount(int eventId) {
        return registrationDAO.getActiveRegistrationCount(eventId);
    }

    public List<Event> getUpcomingEvents() {
        return eventDAO.getUpcomingEvents();
    }

    public List<Registration> getUserRegistrations(int userId) {
        return registrationDAO.getRegistrationsByUserId(userId);
    }

    public List<BonusPoints> getActiveBonusPointsByStudentId(int userId) {
        return bonusDAO.getActiveBonusPointsByStudentId(userId);
    }

    public int getTotalActiveBonusPointsByStudentId(int userId) {
        return bonusDAO.getTotalActiveBonusPointsByStudentId(userId);
    }

    public void addPoints(BonusPoints bonusPoints) {
        bonusDAO.create(bonusPoints);
    }


    public List<User> getUsersForEvent(int eventId) {
        return registrationDAO.getUsersByEventId(eventId);
    }

    public List<BonusPoints> getAllBonusPointsByTeacherId(int userId) {
        return bonusDAO.getAllBonusPointsByTeacherId(userId);
    }

    public List<Registration> getRegistrationByEventId(Integer eventId) {
        return registrationDAO.getRegistrationByEventId(eventId);
    }

    public void addBonusPointsToUsers(int eventId, String[] userIds, String[] userPoints, Date expiryDate) {
        if (userIds == null || userPoints == null || userIds.length == 0 || userPoints.length == 0) {
            throw new IllegalArgumentException("No users selected or points provided.");
        }
        for (int i = 0; i < userIds.length; i++) {
            String userIdStr = userIds[i];
            String userPointsStr = userPoints[i];
            if (userIdStr != null && userPointsStr != null) {
                int userId = Integer.parseInt(userIdStr);
                int userPoint = Integer.parseInt(userPointsStr);
                addPoints(new BonusPoints(new Event(eventId), new User(userId), userPoint, expiryDate));
            }
        }
    }
    public Map<String,Object> getEventContext(Integer eventId, User user){
        Map<String, Object> context = new HashMap<>();
        Event event = get(eventId);
        if (event!=null){
            List<Comment> comments = getCommentsForEvent(eventId);
            context.put("event", event);
            context.put("comments", comments);
            context.put("user", user);
            List<Registration> activeRegistrations = getRegistrationByEventId(eventId);
            context.put("activeRegistrations",activeRegistrations);
            Registration registration = isUserRegistered(eventId, user.getId());
            context.put("registration", registration);
            context.put("eventHasPassed", event.hasPassed());
            int activeRegistrationCount=getActiveRegistrationCount(event.getId());
            context.put("activeRegistrationCount",activeRegistrationCount);
            return context;
        }
        return null;
    }
    public Map<String,Object> getEventsContext(User user){
        Map<String, Object> context = new HashMap<>();
        context.put("events", getAllEvents());
        context.put("user", user);
        return context;
    }
    public Map<String, Object> getBonusPointsContext(User user){
        int userId = user.getId();
        Map<String,Object> context = new HashMap<>();
        context.put("user",user);
        if (user.getRole() == Role.STUDENT) {
            List<BonusPoints> activeBonuses = getActiveBonusPointsByStudentId(userId);
            int totalActivePoints = getTotalActiveBonusPointsByStudentId(userId);
            context.put("activeBonuses", activeBonuses);
            context.put("totalActivePoints", totalActivePoints);
        } else if (user.getRole() == Role.TEACHER) {
            List<BonusPoints> teacherBonuses = getAllBonusPointsByTeacherId(userId);
            context.put("teacherBonuses", teacherBonuses);
        }
        return context;
    }

    public List<Event> searchEvents(String query, String categoryId, String startDate, String endDate, String createdBy,String sortOrder) {
        return eventDAO.searchEvents(query,categoryId,startDate,endDate,createdBy,sortOrder);
    }

}
