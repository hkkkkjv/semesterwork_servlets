package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.Topic;
import ru.kpfu.itis.semester_work.model.User;
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
import java.util.Map;
@WebServlet("/topics")
public class TopicsServlet extends HttpServlet {
    private TopicService topicService;
    private TemplateRenderer templateRenderer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        topicService = (TopicService) getServletContext().getAttribute(ServletContextAttributesNames.TOPIC_SERVICE);
        templateRenderer = (TemplateRenderer)getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> context = new HashMap<>();
        context.put("topics", topicService.getAllTopics());
        context.put("user", req.getSession().getAttribute("user"));
        templateRenderer.render("topics.ftl", context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("topicTitle");
        String description = req.getParameter("topicDescription");
        User user = (User) req.getSession().getAttribute("user");
        if (user!=null){
            Topic topic = topicService.addTopic(title,description,user);
            resp.sendRedirect("/topic?topicId="+topic.getId());
        }
    }
}
