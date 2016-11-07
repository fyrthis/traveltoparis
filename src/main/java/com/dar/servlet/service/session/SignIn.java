package com.dar.servlet.service.session;

import com.dar.Tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

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
        out.println("You called this servlet with get method, feels bad man...");
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        StringBuilder requestDb = new StringBuilder("SELECT password,salt FROM Users WHERE ");
        String uname = request.getParameter("uname");
        String password = request.getParameter("psw");
        String dbHashedPass = null;
        String dbSalt = null;
        requestDb.append("login='").append(uname).append("'");
        try{
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/travelToParisDB");
            if (ds != null) {
                Connection conn = ds.getConnection();
                if (conn != null) {
                    Statement stmt = conn.createStatement();
                    ResultSet rst = stmt.executeQuery(requestDb.toString());
                    if (rst.next()) dbHashedPass = rst.getString("password");
                    if (rst.next()) dbSalt = rst.getString("salt");
                    conn.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace(out);
            return;
        }

        // format: algorithm:iterations:hashSize:salt:hash
        String param = "sha256:" + Tools.PBKDF2_ITERATIONS + ":" + Tools.HASH_BYTE_SIZE + ":" + dbSalt + ":" + dbHashedPass;
        boolean isValid;
        try {
            isValid = Tools.verifyPassword(password, param);
        } catch (Exception e){
            e.printStackTrace();
            e.printStackTrace(out);
            return;
        }
        if(isValid){
            out.println("<html>");
            out.println("<head>");
            out.println("</head>");
            out.println("<body><br>");
            HttpSession session = request.getSession(true);
            if(!session.isNew()){
                session.setAttribute(uname, dbHashedPass);
                out.println("<p>Vous etes connecte</p><br>");
            }
            else{
                out.println("<p>Vous etes deja connecte</p><br>");
            }
        } else {
            out.print("<h1> Erreur - Mauvais Pass <h1><br>");
        }
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
