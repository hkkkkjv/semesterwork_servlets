package ru.kpfu.itis.semester_work.filters;

import ru.kpfu.itis.semester_work.model.User;
import ru.kpfu.itis.semester_work.services.UserService;
import ru.kpfu.itis.semester_work.util.ServletContextAttributesNames;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/forum/*", "/topics/*","/topic", "/profile/*", "/event", "/editProfile", "/myEvents", "/addEvent","/myBonusPoints","/addBonusPoints"})
public class AuthenticateFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        UserService userService = (UserService) getServletContext().getAttribute(ServletContextAttributesNames.USER_SERVICE);
        if (req.getSession().getAttribute("user") != null) {
            chain.doFilter(req, res);
        } else {
            Cookie[] cookies = req.getCookies();
            String username = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("rememberMe".equals(cookie.getName())) {
                        username = cookie.getValue();
                        break;
                    }
                }
            }
            if (username != null) {
                User user = userService.getUserByUsername(username);
                if (user != null) {
                    req.getSession().setAttribute("user", user);
                    chain.doFilter(req, res);
                    return;
                }
            }
            String redirectUrl = req.getRequestURI();
            String query = req.getQueryString() != null ? "?" + req.getQueryString() : "";
            res.sendRedirect("/login?redirect=" + redirectUrl + query);
        }
    }
}
