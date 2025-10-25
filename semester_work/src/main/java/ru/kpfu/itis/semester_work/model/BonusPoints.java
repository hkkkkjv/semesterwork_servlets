package ru.kpfu.itis.semester_work.model;

import java.util.Date;

public class BonusPoints {
    private Integer id;
    private Event event;
    private User student;
    private Integer points;
    private Date date;
    private Date expirationDate;

    public BonusPoints() {
    }

    public BonusPoints(Event event, User student, Integer points, Date expirationDate) {
        this.event = event;
        this.student = student;
        this.points = points;
        this.expirationDate = expirationDate;
    }

    public Integer getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public User getStudent() {
        return student;
    }

    public Integer getPoints() {
        return points;
    }

    public Date getDate() {
        return date;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
