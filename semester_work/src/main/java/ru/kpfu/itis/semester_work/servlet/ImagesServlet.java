package ru.kpfu.itis.semester_work.servlet;

import ru.kpfu.itis.semester_work.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
@WebServlet("/images")
public class ImagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String filename = req.getParameter("image_name");
            File file = new File(Constant.IMAGE_PATH + filename);
            resp.setContentType("image/jpeg");
            resp.setContentLength((int) file.length());
            try(FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream inputStream = new BufferedInputStream(fileInputStream);
            ServletOutputStream servletOutputStream = resp.getOutputStream();){
                int readBytes;
                while ((readBytes = inputStream.read())!=-1){
                    servletOutputStream.write(readBytes);
                }
                servletOutputStream.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
