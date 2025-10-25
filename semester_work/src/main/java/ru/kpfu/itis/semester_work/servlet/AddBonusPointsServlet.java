package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.BonusPoints;
import ru.kpfu.itis.semester_work.model.Event;
import ru.kpfu.itis.semester_work.model.User;
import ru.kpfu.itis.semester_work.services.EventService;
import ru.kpfu.itis.semester_work.util.ServletContextAttributesNames;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/addBonusPoints")
public class AddBonusPointsServlet extends HttpServlet {
    private EventService eventService;
    private TemplateRenderer templateRenderer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        eventService = (EventService) getServletContext().getAttribute(ServletContextAttributesNames.EVENT_SERVICE);
        templateRenderer = (TemplateRenderer) getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> context = new HashMap<>();
        String eventIdStr = req.getParameter("eventId");
        if (eventIdStr != null) {
            try {
                int eventId = Integer.parseInt(eventIdStr);
                Event event = eventService.get(eventId);
                context.put("event", event);
                List<User> users = eventService.getUsersForEvent(eventId);
                context.put("users", users);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid event ID.");
                return;
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Event ID is required.");
            return;
        }
        templateRenderer.render("addBonusPoints.ftl", context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String eventIdStr = req.getParameter("eventId");
        String expiryDateStr = req.getParameter("expiryDate");
        if (eventIdStr == null || expiryDateStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Event ID and points are required.");
            return;
        }
        int eventId;
        Date expiryDate;
        try {
            eventId = Integer.parseInt(eventIdStr);
            expiryDate = parseDate(expiryDateStr);
        } catch (NumberFormatException | ParseException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data.");
            return;
        }
        String[] userIds = req.getParameterValues("userIds");
        String[] userPoints = req.getParameterValues("userPoints");
        try {
            eventService.addBonusPointsToUsers(eventId,userIds,userPoints,expiryDate);
            resp.sendRedirect("/myBonusPoints");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error while adding points");
        }
    }
    private Date parseDate(String expiryDateStr) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(expiryDateStr);
    }
}
