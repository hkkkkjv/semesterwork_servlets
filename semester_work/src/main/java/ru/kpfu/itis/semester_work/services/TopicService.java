package ru.kpfu.itis.semester_work.services;

import ru.kpfu.itis.semester_work.dao.ForumDAO;
import ru.kpfu.itis.semester_work.dao.TopicDAO;
import ru.kpfu.itis.semester_work.model.Forum;
import ru.kpfu.itis.semester_work.model.Topic;
import ru.kpfu.itis.semester_work.model.User;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicService {
    private final TopicDAO topicDAO;
    private final ForumDAO forumDAO;

    public TopicService(TopicDAO topicDAO, ForumDAO forumDAO) {
        this.topicDAO = topicDAO;
        this.forumDAO = forumDAO;
    }

    public List<Topic> getAllTopics() {
        return topicDAO.getAll();
    }

    public Topic addTopic(String title, String description, User user) {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setDescription(description);
        topic.setCreatedBy(user);
        topic = topicDAO.create(topic);
        return topic;
    }

    public Topic get(int topicId) {
        return topicDAO.get(topicId);
    }

    public Map<String,List<Forum>> getForumsByTopicId(int topicId) {
        Map<String,List<Forum>> forumsMap = new HashMap<>();
        forumsMap.put("pinned",forumDAO.getPinnedByTopicId(topicId));
        forumsMap.put("unpinned",forumDAO.getUnpinnedByTopicId(topicId));
        return forumsMap;
    }

    public void createForumPost(String text, User user, int topicId) {
        Forum forum = new Forum(text, user,topicId);
        forumDAO.create(forum);
        Topic topic = topicDAO.get(topicId);
        topic.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        topicDAO.update(topic);
    }
    public List<Topic> getNewTopics(){
        return topicDAO.getNewTopics();
    }

    public void pinForumPost(int forumId) {
        forumDAO.pinForum(forumId);
    }
    public void unpinForumPost(int forumId) {
        forumDAO.unpinForum(forumId);
    }
    public Map<String,Object> getTopicContext(int topicId,User user){
        Topic topic = get(topicId);
        Map<String, List<Forum>> forums = getForumsByTopicId(topicId);
        Map<String, Object> context = new HashMap<>();
        context.put("topic", topic);
        context.put("pinnedForums", forums.get("pinned"));
        context.put("unpinnedForums", forums.get("unpinned"));
        context.put("user", user);
        return context;
    }
}
