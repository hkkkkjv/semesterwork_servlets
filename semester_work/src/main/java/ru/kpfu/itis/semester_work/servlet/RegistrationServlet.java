package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.RegistrationValidator;
import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.model.Role;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/register")

public class RegistrationServlet extends HttpServlet {
    UserService userService;
    private TemplateRenderer templateRenderer;
    private PasswordHasher passwordHasher;
    RegistrationValidator registrationValidator;

    @Override

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        passwordHasher = new PasswordHasher();
        userService = (UserService) getServletContext().getAttribute(ServletContextAttributesNames.USER_SERVICE);
        registrationValidator = new RegistrationValidator(userService);
        templateRenderer = (TemplateRenderer) getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        if ("checkUsername".equals(action)) {
            String username = req.getParameter("username");
            boolean isAvailable = userService.isUsernameAvailable(username);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"available\":" + isAvailable + "}");
            return;
        } else if ("checkEmail".equals(action)) {
            String email = req.getParameter("email");
            boolean isAvailable = userService.isEmailAvailable(email);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"available\":" + isAvailable + "}");
            return;
        }
        Map<String, Object> context = new HashMap<>();
        templateRenderer.render("register.ftl", context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String roleString = request.getParameter("role");
        Map<String, String> validationErrors = registrationValidator.validate(username, email, password, confirmPassword);
        if (!validationErrors.isEmpty()) {
            Map<String, Object> context = new HashMap<>();
            context.put("errors", validationErrors);
            templateRenderer.render("register.ftl", context, response);
            return;
        }
        try {
            userService.createUser(username, email, password, roleString);
            request.setAttribute("success", "Registration successful");
            response.sendRedirect("/login");
        } catch (Exception e) {
            Map<String, Object> context = new HashMap<>();
            context.put("error", "Registration failed");
            templateRenderer.render("register.ftl", context, response);
        }
    }
}
