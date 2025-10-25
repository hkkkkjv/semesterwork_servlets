package ru.kpfu.itis.semester_work.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Event {
    private Integer id;
    private String title;
    private String description;
    private String location;
    private Timestamp time;
    private Integer seatCount;
    private Category category;
    private User createdBy;
    private String imagePath;


    public Event() {
    }

    public Event(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Event(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Event(String title, String description, String location, Timestamp time, Integer seatCount, Category category, User createdBy, String imagePath) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.time = time;
        this.seatCount = seatCount;
        this.category = category;
        this.createdBy = createdBy;
        this.imagePath = imagePath;
    }

    public Event(Integer id, String title, String description, String location, Timestamp time, Integer seatCount, Category category, User createdBy, String imagePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.time = time;
        this.seatCount = seatCount;
        this.category = category;
        this.createdBy = createdBy;
        this.imagePath = imagePath;
    }

    public Event(String title, String description, String location, Timestamp time, int seatCount, Category category, String imagePath) {
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean hasPassed(){
        return LocalDateTime.now().isAfter(time.toLocalDateTime());
    }
    public Event(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getTime() {
        return time;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public Category getCategory() {
        return category;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }
}
