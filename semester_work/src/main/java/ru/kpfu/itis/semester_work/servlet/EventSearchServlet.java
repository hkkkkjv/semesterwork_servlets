package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.services.EventService;
import ru.kpfu.itis.semester_work.services.UserService;
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

@WebServlet("/eventSearch")
public class EventSearchServlet extends HttpServlet {
    private EventService eventService;
    private UserService userService;
    private TemplateRenderer templateRenderer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        eventService= (EventService) getServletContext().getAttribute(ServletContextAttributesNames.EVENT_SERVICE);
        userService= (UserService) getServletContext().getAttribute(ServletContextAttributesNames.USER_SERVICE);
        templateRenderer = (TemplateRenderer) getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,Object> context = new HashMap<>();
        context.put("categories",eventService.getAllCategories());
        context.put("creators",userService.getAllTeachers());
        templateRenderer.render("eventSearch.ftl", context, resp);
    }
}
