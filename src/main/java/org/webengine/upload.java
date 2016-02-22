package org.webengine;

import javax.servlet.MultipartConfigElement;
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
 
@WebServlet(name = "FileUploadServlet", urlPatterns = "/upload")
@MultipartConfig
public  class upload extends HttpServlet {
 
    private static final File UPLOAD_DEST = new File("/home/student/workspace/Team1/foratest");
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
                                     throws ServletException, IOException {
        final String fileName = req.getParameter("fileName");
        final File destFile = new File(UPLOAD_DEST, fileName);
 
        final Part filePart = req.getPart("uploadedFile");        final InputStream in = filePart.getInputStream();        Files.copy(in, destFile.toPath());
 
        resp.getWriter().append("File successfully uploaded!");
    }
}