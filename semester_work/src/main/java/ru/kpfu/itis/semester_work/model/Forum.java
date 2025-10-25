package ru.kpfu.itis.semester_work.model;

import java.sql.Timestamp;

public class Forum {
    private Integer id;
    private String text;
    private User user;
    private Timestamp date;
    private Integer topicId;
    private boolean isPinned;

    public Forum(String text, User user, Integer topicId) {
        this.text = text;
        this.user = user;
        this.topicId = topicId;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public Forum(Integer id, String text, User user, Timestamp date, Integer topicId, boolean isPinned) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.date = date;
        this.topicId = topicId;
        this.isPinned = isPinned;
    }

    public Forum(Integer id, String text, User user, Timestamp date) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.date = date;
    }

    public Forum(String text, User user) {
        this.text = text;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", user=" + user +
                ", date=" + date +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public Timestamp getDate() {
        return date;
    }

    public Integer getId() {
        return id;
    }
}
