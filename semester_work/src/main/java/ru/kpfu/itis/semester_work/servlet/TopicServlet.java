package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.Forum;
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
import java.util.List;
import java.util.Map;

@WebServlet("/topic")
public class TopicServlet extends HttpServlet {
    private TopicService topicService;
    private TemplateRenderer templateRenderer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        topicService = (TopicService) getServletContext().getAttribute(ServletContextAttributesNames.TOPIC_SERVICE);
        templateRenderer = (TemplateRenderer) getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String topicIdString = request.getParameter("topicId");
        if (topicIdString == null || topicIdString.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Event ID is required");
            return;
        }
        try {
            int topicId = Integer.parseInt(topicIdString);
            User user = (User) request.getSession().getAttribute("user");
            templateRenderer.render("topic.ftl", topicService.getTopicContext(topicId, user), response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Event ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String topicIdString = request.getParameter("topicId");
        User user = (User) request.getSession().getAttribute("user");
        String action = request.getParameter("action");
        try {
            if ("pin".equals(action) && user != null) {
                handlePinForumPost(request, response);
            } else if ("unpin".equals(action) && user != null) {
                handleUnpinForumPost(request, response);
            } else {
                int topicId = Integer.parseInt(topicIdString);
                handleCreateForumPost(request, response, topicId, user);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Topic ID format");
        }
    }

    private void handleCreateForumPost(HttpServletRequest request, HttpServletResponse response, int topicId, User user) throws IOException {
        String anonymousParam = request.getParameter("anonymous");
        String text = request.getParameter("text");
        boolean isAnonymous = anonymousParam != null;
        topicService.createForumPost(text, isAnonymous ? null : user, topicId);
        response.sendRedirect(request.getContextPath() + "topic?topicId=" + topicId);
    }

    private void handlePinForumPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String forumIdString = request.getParameter("forumId");
        if (forumIdString != null) {
            try {
                int forumId = Integer.parseInt(forumIdString);
                topicService.pinForumPost(forumId);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid forum ID format");
            }
        }
    }

    private void handleUnpinForumPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String forumIdString = request.getParameter("forumId");
        System.out.println(forumIdString);
        if (forumIdString != null) {
            try {
                int forumId = Integer.parseInt(forumIdString);
                topicService.unpinForumPost(forumId);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid forum ID format");
            }
        }
    }
}
