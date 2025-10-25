package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.User;
import ru.kpfu.itis.semester_work.services.UserService;
import ru.kpfu.itis.semester_work.util.PasswordHasher;
import ru.kpfu.itis.semester_work.util.ServletContextAttributesNames;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/editProfile")
public class EditProfileServlet extends HttpServlet {
    private UserService userService;
    private TemplateRenderer templateRenderer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) getServletContext().getAttribute(ServletContextAttributesNames.USER_SERVICE);
        templateRenderer = (TemplateRenderer) getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        User user = (User) req.getSession().getAttribute("user");
        User currentUser = userService.get(user.getId());
        if ("checkUsername".equals(action)) {
            String username = req.getParameter("username");
            boolean isAvailable = userService.isUsernameAvailable(username, user.getId());
            resp.setContentType("application/json");
            resp.getWriter().write("{\"available\":" + isAvailable + "}");
            return;
        } else if ("checkEmail".equals(action)) {
            String email = req.getParameter("email");
            boolean isAvailable = userService.isEmailAvailable(email, user.getId());
            resp.setContentType("application/json");
            resp.getWriter().write("{\"available\":" + isAvailable + "}");
            return;
        }
        Map<String, Object> context = new HashMap<>();
        context.put("user", currentUser);
        templateRenderer.render("editProfile.ftl", context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");
        User user = userService.get(sessionUser.getId());
        if (user != null) {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String about = request.getParameter("about");
            userService.updateUserProfile(user,username,email,about,password);
        }
        if (user!=null){
            User currentUser = userService.get(user.getId());
            sessionUser.setUsername(currentUser.getUsername());
            session.setAttribute("user", sessionUser);
            response.sendRedirect("/profile?id=" + user.getId());
        }
    }
}

