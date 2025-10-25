package ru.kpfu.itis.semester_work;

import ru.kpfu.itis.semester_work.dao.*;
import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.User;
import ru.kpfu.itis.semester_work.services.*;
import ru.kpfu.itis.semester_work.util.ServletContextAttributesNames;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        TemplateRenderer templateRenderer = new TemplateRenderer(sce.getServletContext());
        sce.getServletContext().setAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER,templateRenderer);
        Connection connection = ConnectionWrapper.getConnection();
        UserDAO userDAO = new UserDAO(connection);
        EventDAO eventDAO = new EventDAO(connection);
        ForumDAO forumDAO = new ForumDAO(connection);
        CommentDAO commentDAO = new CommentDAO(connection);
        TopicDAO topicDAO = new TopicDAO(connection);
        CategoryDAO categoryDAO = new CategoryDAO(connection);
        RegistrationDAO registrationDAO = new RegistrationDAO(connection);
        BonusDAO bonusDAO = new BonusDAO(connection);
        UserService userService = new UserService(userDAO);
        EventService eventService = new EventService(eventDAO,registrationDAO,commentDAO,categoryDAO,bonusDAO);
        TopicService topicService = new TopicService(topicDAO,forumDAO);
        sce.getServletContext().setAttribute(ServletContextAttributesNames.USER_SERVICE, userService);
        sce.getServletContext().setAttribute(ServletContextAttributesNames.EVENT_SERVICE, eventService);
        sce.getServletContext().setAttribute(ServletContextAttributesNames.TOPIC_SERVICE, topicService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Connection connection = (Connection) sce.getServletContext().getAttribute("connection");
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
