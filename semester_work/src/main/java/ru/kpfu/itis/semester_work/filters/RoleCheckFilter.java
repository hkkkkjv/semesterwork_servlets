package ru.kpfu.itis.semester_work.filters;

import ru.kpfu.itis.semester_work.model.Role;
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

@WebFilter(urlPatterns = {"/addEvent","/addBonuses"})
public class RoleCheckFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        User user= (User) req.getSession().getAttribute("user");
        if (user!=null&& user.getRole().equals(Role.TEACHER)){
            chain.doFilter(req,res);
        }else {
            res.sendRedirect(req.getContextPath()+"/accessDenied");
        }
    }
}
