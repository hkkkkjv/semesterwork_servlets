package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.Event;
import ru.kpfu.itis.semester_work.model.Topic;
import ru.kpfu.itis.semester_work.model.User;
import ru.kpfu.itis.semester_work.services.EventService;
import ru.kpfu.itis.semester_work.services.TopicService;
import ru.kpfu.itis.semester_work.util.ServletContextAttributesNames;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@WebServlet("/")
public class HomeServlet extends HttpServlet {
    private EventService eventService;
    private TopicService topicService;
    private TemplateRenderer templateRenderer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        eventService = (EventService) getServletContext().getAttribute(ServletContextAttributesNames.EVENT_SERVICE);
        topicService = (TopicService) getServletContext().getAttribute(ServletContextAttributesNames.TOPIC_SERVICE);
        templateRenderer = (TemplateRenderer) getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> context = new HashMap<>();
        List<Event> upcomingEvents = eventService.getUpcomingEvents();
        List<Topic> newTopics = topicService.getNewTopics();
        context.put("upcomingEvents", upcomingEvents);
        context.put("newTopics", newTopics);
        User user = (User) req.getSession().getAttribute("user");
        String username = user!=null?user.getUsername():null;
        String greeting = (username != null ? "Hello, " + username + "! " : "") + "Welcome to the University Events Portal";
        context.put("greeting", greeting);
        context.put("user",user);
        templateRenderer.render("home.ftl",context,resp);
    }
}
