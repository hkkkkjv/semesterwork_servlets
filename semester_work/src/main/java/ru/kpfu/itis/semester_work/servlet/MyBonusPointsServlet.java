package ru.kpfu.itis.semester_work.servlet;


import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.BonusPoints;
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
@WebServlet("/myBonusPoints")
public class MyBonusPointsServlet extends HttpServlet {
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
        User user = (User)req.getSession().getAttribute("user");
        templateRenderer.render("myBonusPoints.ftl",eventService.getBonusPointsContext(user),resp);
    }
}
