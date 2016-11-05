package com.dar.servlet.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class GetImages extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletContext cntx = req.getServletContext();
        Enumeration<String> pNames = req.getParameterNames();
        // Get the absolute path of the image
        String filename = "/opt/tomcat/webres/" + (req.getParameter(pNames.nextElement())) + ".jpg";
        // retrieve mimeType dynamically
        String mime = cntx.getMimeType(filename);
        if (mime == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        resp.setContentType(mime);
        File file = new File(filename);
        resp.setContentLength((int)file.length());

        FileInputStream in = new FileInputStream(file);
        OutputStream out = resp.getOutputStream();
        // Copy the contents of the file to the output stream
        byte[] buf = new byte[1024];
        int count;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.close();
        in.close();
    }
}
