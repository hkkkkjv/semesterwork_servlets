package ru.kpfu.itis.semester_work.model;

import java.sql.Timestamp;

public class Registration {
    private Integer id;
    private User user;
    private Event event;
    private Timestamp registrationTime;
    private Status status;
    private String comment;

    public Registration() {
    }

    public Registration(Integer id, User user, Event event, Timestamp registrationTime, Status status, String comment) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.registrationTime = registrationTime;
        this.status = status;
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    public Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public Status getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
