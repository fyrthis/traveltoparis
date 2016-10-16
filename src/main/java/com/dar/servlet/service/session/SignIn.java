package com.dar.servlet.service.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SignIn extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("You called this servlet with get method");
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        StringBuilder errors = new StringBuilder();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuilder requestDb = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            //TODO : stuff
        }
    }
}
