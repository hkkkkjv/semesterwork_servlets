package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.Category;
import ru.kpfu.itis.semester_work.model.Event;
import ru.kpfu.itis.semester_work.model.User;
import ru.kpfu.itis.semester_work.services.EventService;
import ru.kpfu.itis.semester_work.util.Constant;
import ru.kpfu.itis.semester_work.util.ServletContextAttributesNames;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/addEvent")
@MultipartConfig(maxFileSize = 1024 * 1024 * 100, maxRequestSize = 1024 * 1024 * 200, fileSizeThreshold = 1024 * 1024)
public class AddEventServlet extends HttpServlet {
    private EventService eventService;
    private TemplateRenderer templateRenderer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        eventService = (EventService) getServletContext().getAttribute(ServletContextAttributesNames.EVENT_SERVICE);
        templateRenderer = (TemplateRenderer) getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> context = new HashMap<>();
        User user = (User) request.getSession().getAttribute("user");
        context.put("user", user);
        context.put("categories", eventService.getAllCategories());
        templateRenderer.render("addEvent.ftl", context, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        int seatCount = Integer.parseInt(request.getParameter("seatCount"));
        int categoryId = Integer.parseInt(request.getParameter("category"));
        Timestamp time = Timestamp.valueOf(LocalDateTime.parse(request.getParameter("eventTime")));
        Part filePart = request.getPart("eventImage");
        User user = (User) request.getSession().getAttribute("user");
        try {
            Event event = eventService.addEvent(title, description, location, time, seatCount, new Category(categoryId), user, filePart);
            response.sendRedirect("/event?id=" +event.getId());
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Error while adding event:"+e.getMessage());
        }
    }


}
