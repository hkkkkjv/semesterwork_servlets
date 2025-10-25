package ru.kpfu.itis.semester_work.helpers;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TemplateRenderer {
    private final Configuration cfg;

    public TemplateRenderer(ServletContext servletContext) {
        cfg = new Configuration();
        cfg.setServletContextForTemplateLoading(servletContext, "/WEB-INF/templates");
        cfg.setDefaultEncoding("UTF-8");
    }

    public void render(String templateName, Map<String, Object> context, HttpServletResponse response) throws IOException {
        Template template;
        try {
            template = cfg.getTemplate(templateName);
            template.process(context, response.getWriter());
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
