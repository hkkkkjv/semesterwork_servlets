package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.Event;
import ru.kpfu.itis.semester_work.model.Registration;
import ru.kpfu.itis.semester_work.model.Role;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@WebServlet("/myEvents")
public class MyEventsServlet extends HttpServlet {
    private EventService eventService;

    private TemplateRenderer templateRenderer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        eventService = (EventService) getServletContext().getAttribute(ServletContextAttributesNames.EVENT_SERVICE);
        templateRenderer = (TemplateRenderer)getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        Map<String, Object> context = new HashMap<>();
        context.put("user",user);
        if (user.getRole() == Role.STUDENT) {
            List<Registration> registrations = eventService.getUserRegistrations(user.getId());
            context.put("registrations", registrations);
        } else if (user.getRole() == Role.TEACHER) {
            List<Event> events = eventService.getUserCreatedEvents(user.getId());
            context.put("events", events);
        }
        templateRenderer.render("myEvents.ftl",context,resp);
    }
}
