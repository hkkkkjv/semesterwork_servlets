package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.helpers.TemplateRenderer;
import ru.kpfu.itis.semester_work.services.UserService;
import ru.kpfu.itis.semester_work.util.PasswordHasher;
import ru.kpfu.itis.semester_work.util.ServletContextAttributesNames;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    UserService userService ;
    private TemplateRenderer templateRenderer;
    private PasswordHasher passwordHasher;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        passwordHasher = new PasswordHasher();
        userService= (UserService) getServletContext().getAttribute(ServletContextAttributesNames.USER_SERVICE) ;
        templateRenderer = (TemplateRenderer)getServletContext().getAttribute(ServletContextAttributesNames.TEMPLATE_RENDERER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(req.getSession().getAttribute("user")!=null){
            resp.sendRedirect(req.getContextPath()+"/");
        }
        Map<String, Object> context = new HashMap<>();
        templateRenderer.render("login.ftl", context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean rememberMe = request.getParameter("rememberMe") != null;
        String redirectUrl = request.getParameter("redirect");
        String hashedPassword = passwordHasher.hashPassword(password);
        try {
            if (userService.login(request,username,hashedPassword)) {
                if (rememberMe) {
                    Cookie cookie = new Cookie("rememberMe", username);
                    cookie.setMaxAge(30 * 24 * 60 * 60);
                    response.addCookie(cookie);
                }
                response.sendRedirect(redirectUrl != null && !redirectUrl.isEmpty() ? request.getContextPath()+redirectUrl : "/");
            } else {
                Map<String, Object> context = new HashMap<>();
                context.put("error", "Invalid username or password");
                templateRenderer.render("login.ftl", context, response);
            }
        } catch (Exception e) {
            Map<String, Object> context = new HashMap<>();
            context.put("error", "Invalid username or password");
            templateRenderer.render("login.ftl", context, response);
        }
    }
}
