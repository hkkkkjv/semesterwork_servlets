package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.*;
import ru.kpfu.itis.semester_work.services.EventService;
import ru.kpfu.itis.semester_work.util.ServletContextAttributesNames;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/event")
public class EventServlet extends HttpServlet {
    private EventService eventService;
    private TemplateRenderer templateRenderer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        eventService = (EventService) getServletContext().getAttribute(ServletContextAttributesNames.EVENT_SERVICE);
        templateRenderer = (TemplateRenderer)getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String eventIdString = req.getParameter("id");
        if (eventIdString == null || eventIdString.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Event ID is required");
            return;
        }
        try {
            int eventId = Integer.parseInt(eventIdString);
            User user = (User) req.getSession().getAttribute("user");
            Map<String, Object> context = eventService.getEventContext(eventId, user);
            if (context != null) {
                templateRenderer.render("event.ftl", context, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Event not found");
            }
        } catch (
                NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Event ID format");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        User user = (User) req.getSession().getAttribute("user");
        if (action != null) {
            switch (action) {
                case "register":
                case "addComment":
                case "deleteComment":
                case "cancelRegistration":
                    handleAction(action, req, resp, user);
                    break;
                default:
                    sendError(resp, "Invalid action");
            }
        }else {
            sendError(resp, "Action is required");
        }
    }


    private void handleAction(String action, HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        String eventId = req.getParameter("eventId");
        switch (action) {
            case "register":
                String comment = req.getParameter("commentText");
                eventService.registerUser(Integer.valueOf(eventId), user.getId(), comment);
                resp.setStatus(HttpServletResponse.SC_OK);
                break;
            case "addComment":
                String commentText = req.getParameter("commentText");
                eventService.addComment(Integer.parseInt(eventId), commentText, user);
                resp.setStatus(HttpServletResponse.SC_OK);
                break;
            case "deleteComment":
                String commentIdString = req.getParameter("commentId");
                if (commentIdString != null && !commentIdString.isEmpty()) {
                    try {
                        int commentId = Integer.parseInt(commentIdString);
                        eventService.deleteComment(commentId);
                        resp.setStatus(HttpServletResponse.SC_OK);
                    } catch (NumberFormatException e) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid comment ID");
                    }
                }
                break;
            case "cancelRegistration":
                String registrationIdString = req.getParameter("registrationId");
                if (registrationIdString!=null&&!registrationIdString.isEmpty()){
                    try{
                        int registrationId = Integer.parseInt(registrationIdString);
                        eventService.cancelRegistration(registrationId);
                        resp.setStatus(HttpServletResponse.SC_OK);
                    }catch (NumberFormatException e){
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid registration ID");
                    }
                }else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Registration ID is required");
                }
                break;
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
    }

}
