package ru.kpfu.itis.semester_work.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.semester_work.model.Event;
import ru.kpfu.itis.semester_work.services.EventService;
import ru.kpfu.itis.semester_work.util.ServletContextAttributesNames;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/ajaxEventSearch")
public class AjaxEventSearchServlet extends HttpServlet {
    private EventService eventService;
    private ObjectMapper objectMapper;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        objectMapper = new ObjectMapper();
        eventService = (EventService) getServletContext().getAttribute(ServletContextAttributesNames.EVENT_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchQuery = req.getParameter("query");
        String categoryId = req.getParameter("categoryId");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String createdBy = req.getParameter("createdBy");
        String sortOrder = req.getParameter("sortOrder");
        if (searchQuery==null||searchQuery.trim().isEmpty()){
            List<Event> list = new ArrayList<>();
            sendResponse(resp,list);
            return;
        }
        List<Event> events = eventService.searchEvents(searchQuery,categoryId,startDate,endDate,createdBy,sortOrder);
        sendResponse(resp,events);
    }
    private void sendResponse(HttpServletResponse response,List<Event> events)throws IOException{
        String result =objectMapper.writeValueAsString(events);
        response.getWriter().println(result);
    }
}
