package com.dar.servlet.service.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;


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
        StringBuilder requestDb = new StringBuilder("SELECT password FROM Users WHERE ");
        String uname = request.getParameter("uname");
        String pass = request.getParameter("psw");
        String dbPass = "jello";
        requestDb.append("login='").append(uname).append("'");
        //TODO le traitement du password
        try{
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/travelToParisDB");
            if (ds != null)
            {
                Connection conn = ds.getConnection();
                errors.append("ds not null | ");
                if (conn != null)
                {
                    errors.append(" conn");
                    Statement stmt = conn.createStatement();
                    ResultSet rst = stmt.executeQuery(requestDb.toString());
                    if (rst.next()) dbPass = rst.getString("password");
                    conn.close();
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            errors.append("failure + ").append(e.getMessage());
        }
        // TODO: remplacer le true par la verification du password (ou pas)
        HttpSession session = request.getSession(true);
        if(!session.isNew()){
            session.setAttribute(uname, dbPass);
            out.println("<p>Vous etes connecte</p><br>");
        }
        else{
            //TODO: erreur
            out.println("<p>Vous etes deja connecte</p><br>");
        }
        out.println(errors.toString()+" | equal " + dbPass.equals(pass) + " | name " + uname + " | pass " + pass.length() + " | passDB " +
                dbPass.length() + " | request " + requestDb.toString());
    }
}
