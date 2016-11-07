package com.dar.servlet.service.update;

import com.dar.Tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class CreateAccount extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("You called this servlet with get method... bad person!");
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("login");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String birth = request.getParameter("birthday");
        String country = request.getParameter("country");
        String password = request.getParameter("password");
        String securePass = "NULL", salt = "NULL";

        Calendar cal = Calendar.getInstance();
        StringBuilder errors = new StringBuilder();
        PrintWriter out = response.getWriter();
        String rq = ("INSERT INTO Users (login, password, salt, firstname, lastname, birthday, country, email) VALUES (?,?,?,?,?,?,?,?);");
        PreparedStatement stmt = null;
        try {
            String[] list = birth.split("-");
            cal.set(Calendar.YEAR, Integer.parseInt(list[0]));
            cal.set(Calendar.MONTH, Integer.parseInt(list[1]));
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(list[2]));
            String hashedReturn = Tools.createHash(password);
            list = hashedReturn.split(":");
            salt = list[3];
            securePass = list[4];
            response.setContentType("text/html");
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/travelToParisDB");
            if (ds != null) {
                Connection conn = ds.getConnection();
                if (conn != null) {
                    stmt = conn.prepareStatement(rq);
                    stmt.setString(1, username);
                    stmt.setString(2, securePass);
                    stmt.setString(3, salt);
                    stmt.setString(4, firstname);
                    stmt.setString(5, lastname);
                    stmt.setDate(6, new Date(cal.getTimeInMillis()));
                    stmt.setString(7, country);
                    stmt.setString(8, email);
                    stmt.executeUpdate();
                    conn.close();
                } else errors.append("Connection to database failed.<br>");
            } else errors.append("Datasource is null.<br>");
        }
        catch (Exception e) {
            errors.append("Query ended with an error : <br>");
            errors.append(rq).append("<br>");
            e.printStackTrace(out);
        }
        out.println("<html>");
        out.println("<head>");
        out.println("</head>");
        out.println("<body><br>");
        if(errors.length() > 0) {
            out.println(errors.toString());
            if(stmt != null) out.println(stmt.toString());
        }
        else {
            out.print("Success !");
        }
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

}
