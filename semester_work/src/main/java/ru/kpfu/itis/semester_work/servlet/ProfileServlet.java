package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.User;
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
import java.util.Objects;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private TemplateRenderer templateRenderer;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) getServletContext().getAttribute(ServletContextAttributesNames.USER_SERVICE);
        templateRenderer = (TemplateRenderer) getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdParam = req.getParameter("id");
        User currentUser = (User) req.getSession().getAttribute("user");
        User userProfile;
        if (userIdParam==null ||userIdParam.isEmpty()) {
            userProfile = userService.get(currentUser.getId());
        } else {
            Integer userId = Integer.parseInt(userIdParam);
            userProfile = userService.get(userId);
        }
        if (userProfile != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("userProfile", userProfile);
            context.put("isCurrentUser", currentUser.getId().equals(userProfile.getId()));
            templateRenderer.render("profile.ftl", context, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
